package stocks;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import companies.Company;
import model.TopicValue;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Stock implements Runnable {

    private Session session;
    private Topic topic;
    private Connection connection;
    private XStream xstream;

    private  Map<Integer, Company> requestsMap;
    private  Map<Integer, Company> offersMap;
    private static  ArrayList<Company> transactionList;

    public Stock(javax.jms.Connection connection) throws Exception{
        this.connection = connection;
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.topic = session.createTopic("transaction");
        this.xstream = new XStream(new StaxDriver());
    }

    public void run() {
        Connection conn = null;
        try {
            ActiveMQTextMessage msg;
            ActiveMQMessageConsumer consumer = (ActiveMQMessageConsumer) session.createConsumer(topic);

            connection.start();

            while ((msg = (ActiveMQTextMessage) consumer.receive()) != null) {

                TopicValue value = (TopicValue) xstream.fromXML(msg.getText());
                System.out.println("Received message is: " + msg.getText());
                // Call your service and ack the message if successfully processed
                msg.acknowledge();

                if(value.seller){
                    offersMap.put(value.id, value.company);
                } else {
                    requestsMap.put(value.id, value.company);
                }

                Iterator it = requestsMap.entrySet().iterator(); //Luam toate cererile curente.

                while (it.hasNext()) {
                    Map.Entry<Integer, Company> requestStock = (Map.Entry<Integer, Company>) it.next();

                    int sellerId = findSellerForStockRequest(requestStock.getValue());

                    if (sellerId != -1) {
                        Company offerStock = offersMap.get(sellerId);
                        int stockAmount = offerStock.getStockAmount();
                        int requestAmount = requestStock.getValue().getStockAmount();
                        //Daca numarul de actiuni din cerere este egal cu cel din oferta vom face vanzarea si vom sterge oferta respectiva deoarece nu vor mai exista actiuni in ea
                        if (stockAmount == requestAmount) {
                            addTransaction(offerStock);
                            offersMap.remove(sellerId);
                            it.remove();
                            System.out.println("Buyer " + requestStock.getKey() + " bought " + stockAmount + " stocks from " + offerStock.getCompanyName() + " at " + offerStock.getStockPrice() + "\n");

                            //Daca numarul de actiuni din cerere este mai mic decat cel din oferta vom face vanzarea si vom substrage numarul de actiuni vandute din oferta
                        } else if (stockAmount > requestAmount) {
                            addTransaction(requestStock.getValue());
                            it.remove();
                            offerStock.setStockAmount(stockAmount - requestAmount);
                            System.out.println("Buyer " + requestStock.getKey() + " bought " + stockAmount + " stocks from " + offerStock.getCompanyName() + " at " + offerStock.getStockPrice() + "\n");
                        }

                        //Daca numarul de actiuni din cerere este mai mare decat cel din oferta vom face vanzarea si vom substrage numarul de actiuni vandute din cerere
                        else {
                            addTransaction(requestStock.getValue());
                            offersMap.remove(sellerId);
                            requestStock.getValue().setStockAmount(requestAmount - stockAmount);
                            offerStock.setStockAmount(stockAmount - requestAmount);
                            System.out.println("Buyer " + requestStock.getKey() + " bought " + stockAmount + " stocks from " + offerStock.getCompanyName() + " at " + offerStock.getStockPrice() + "\n");
                        }
                    }
                }
            }

        } catch (Exception ignored) {

        } finally {
        }
    }

    private Integer findSellerForStockRequest(Company request){
        for (Map.Entry<Integer, Company> offerStock : offersMap.entrySet()) {
            Company offer = offerStock.getValue();
            if(offer.getCompanyName().equals(request.getCompanyName()) && offer.getStockPrice() == request.getStockPrice()) {
                return offerStock.getKey();
            }
        }
        return -1;
    }

    public ArrayList<Company> getTransactionList() {
        return transactionList;
    }

    private synchronized void addTransaction(Company company){
        transactionList.add(company);
    }

}



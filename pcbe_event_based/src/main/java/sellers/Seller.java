package sellers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import companies.Company;
import model.TopicValue;
import utils.RandomRange;

import javax.jms.*;

public class Seller implements Runnable{

    private static int totalSellers = 0;
    private int sellerID;
    private String companyName;
    private Session session;
    private Topic topic;
    private XStream xstream;

    public Seller(String companyName, Connection connection) throws Exception{
        totalSellers++;
        this.sellerID = totalSellers;
        this.companyName = companyName;
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.topic = session.createTopic("transaction");
        this.xstream = new XStream(new StaxDriver());
    }

    private void addAnOffer(int sellerID, Company company) throws Exception{
        TopicValue value = new TopicValue(sellerID, false, company);
        TextMessage message = session.createTextMessage(xstream.toXML(value));
        MessageProducer publisher = session.createProducer(topic);
        System.out.println("Sending text '" + value + "'");
        publisher.send(message, javax.jms.DeliveryMode.PERSISTENT, javax.jms.Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
    }

    public void run() {
        System.out.println("Seller " + sellerID + " started!");
        try {
            while(true) {
                Company stockToSell = new Company(this.companyName);
                addAnOffer(sellerID, stockToSell);

                int sleepTime = RandomRange.randomWithRange(1, 3)*1000;
                Thread.sleep(sleepTime);
            }
        }catch(Exception e) {
            System.out.println(this);
        }
    }

}

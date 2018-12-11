import java.net.URI;
import java.util.ArrayList;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import buyers.Buyer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import sellers.Seller;
import stocks.Stock;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection connection = null;
        try {

            BrokerService broker = BrokerFactory.createBroker(new URI("broker:(tcp://localhost:61616)"));
            broker.start();

            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connection = connectionFactory.createConnection();
            connection.setClientID("DurabilityTest");


            ArrayList<Thread> sellers = new ArrayList<Thread>();
            ArrayList<Thread> buyers = new ArrayList<Thread>();

            Stock stock = new Stock(connection);

            Thread stockThread = new Thread(stock);

            Seller seller1 = new Seller("Nokia", connection);
            Seller seller2 = new Seller("Hella", connection);

            Buyer buyer1 = new Buyer("Nokia", connection);
            Buyer buyer2 = new Buyer("Hella", connection);
            Buyer buyer3 = new Buyer("Conti", connection);


            Thread s1 = new Thread(seller1);
            Thread s2 = new Thread(seller2);

            sellers.add(s1);
            sellers.add(s2);

            Thread b1 = new Thread(buyer1);
            Thread b2 = new Thread(buyer2);
            Thread b3 = new Thread(buyer3);

            buyers.add(b1);
            buyers.add(b2);
            buyers.add(b3);

            stockThread.start();

            for(Thread seller : sellers) {
                seller.start();
            }

            for(Thread buyer : buyers) {
                buyer.start();
            }

            try {
                Thread.sleep(10000);

                for(Thread seller : sellers) {
                    if(seller.isAlive()) { //verificare daca thread-ul inca ruleaza
                        seller.interrupt(); //thread-ul este intrerupt
                    }
                }

                for(Thread buyer : buyers) {
                    if(buyer.isAlive()) {
                        buyer.interrupt();
                    }
                }

                stockThread.interrupt();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
/*
Cum functioneaza:
    - avem doi producatori de evenimente si un subscriber
    - cumparatorii: anunta ca doresc sa cumpere actiuni
    - vanzatorii: anunta ca doresc sa vanda actiuni
    - cele doua categorii scriu in doua topicuri separate: buyerTopic, sellerTopic
    - bursa este abonata la cele doua topicuri, citeste ofertele si cererile, si realizeaza tranzactiile.
 */

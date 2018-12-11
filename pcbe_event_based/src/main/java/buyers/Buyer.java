package buyers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import companies.Company;
import model.TopicValue;
import utils.RandomRange;
import javax.jms.*;


public class Buyer implements Runnable {

    private static int totalBuyers = 0;
    private String companyName;
    private int buyerID;
    private Session session;
    private Topic topic;
    private XStream xstream;

    public Buyer(String companyName, Connection connection) throws Exception{
        totalBuyers++;
        this.companyName = companyName;
        this.buyerID = totalBuyers;
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.topic = session.createTopic("transaction");
        this.xstream = new XStream(new StaxDriver());
    }

    private void addARequest(int buyerId, Company request) throws Exception{
        TopicValue value = new TopicValue(buyerId, true, request);
        TextMessage message = session.createTextMessage(xstream.toXML(value));
        MessageProducer publisher = session.createProducer(topic);
        System.out.println("Sending text '" + message + "'");
        publisher.send(message, javax.jms.DeliveryMode.PERSISTENT, javax.jms.Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
    }

    public void run() {
        System.out.println("Buyer " + buyerID + " started!");
        try {
            while (true) {
                Company request = new Company(this.companyName);
                addARequest(buyerID, request);

                int sleepTime = RandomRange.randomWithRange(2, 6) * 1000;
                Thread.sleep(sleepTime);
            }
        } catch (Exception e) {
            System.out.println(this);
        }
    }

}
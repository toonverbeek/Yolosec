/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptsesd.groepb.shared.jms;

import com.ptsesd.groepb.shared.ItemComm;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Tim
 */
public abstract class MessagingGateway {

    private Connection connection;
    protected Session session;
    private MessageProducer mProducer;
    private MessageConsumer mConsumer;
    private static String factoryName = "queueConnectionFactory";
    private Context jndiContext;
    private String requestQueue;
    private String replyQueue;

    public MessagingGateway(String requestQueue, String replyQueue) {
        this.requestQueue = requestQueue;
        this.replyQueue = replyQueue;
        setupConnection();
    }

    private void setupConnection() {
        try {
            Properties prop = new Properties();
            prop.put(Context.PROVIDER_URL, "https://192.168.24.11:4848");
            jndiContext = new InitialContext(prop);
            ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup(factoryName);
            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            setDestinations();
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(MessagingGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setDestinations() {
        try {
            Destination sendToDestination = (Destination) jndiContext.lookup(requestQueue);
            mProducer = session.createProducer(sendToDestination);

            Destination replyFromDestination = (Destination) jndiContext.lookup(replyQueue);
            mConsumer = session.createConsumer(replyFromDestination);
            mConsumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message message) {
                    onReceivedMessage(message);
                }
            });
        } catch (JMSException | NamingException ex) {
            Logger.getLogger(MessagingGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public abstract void onReceivedMessage(Message msg);

    public void sendItemComm(ItemComm item) {
        Message msg = null;
        ItemSerializer serializer = new ItemSerializer();
        String json = serializer.itemToJson(item);
        try {
            msg = session.createTextMessage(json);
            this.mProducer.send(msg);
        } catch (JMSException ex) {
            Logger.getLogger(MessagingGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openConnection() {
        try {
            if (connection != null) {
                connection.start();
            }
        } catch (JMSException ex) {
            Logger.getLogger(MessagingGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

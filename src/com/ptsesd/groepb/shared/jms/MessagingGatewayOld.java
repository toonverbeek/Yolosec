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
public abstract class MessagingGatewayOld {

    private Connection connection;
    protected Session session;
    private MessageProducer mProducer;
    private MessageConsumer mConsumer;
    private static String factoryName = "queueConnectionFactory";
    private Context jndiContext;
    private String requestQueue;
    private String replyQueue;

    public MessagingGatewayOld(String requestQueue, String replyQueue) {
        this.requestQueue = requestQueue;
        this.replyQueue = replyQueue;
        setupConnection();
    }

    private void setupConnection() {
        try {
            Properties prop = new Properties();
//            prop.put("org.omg.CORBA.ORBInitialHost", "192.168.24.11");
//            prop.put("org.omg.CORBA.ORBInitialPort", "4848");

            jndiContext = new InitialContext();
            System.out.println("before lookup2");
            if (jndiContext.getEnvironment() == null) {
                System.out.println("environment is null");
            }

            System.out.println("factory.initial: " + jndiContext.getEnvironment().get("java.naming.factory.initial"));
            ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("adfadsf");
            System.out.println("after lookup");
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
                    try {
                        ItemSerializer serializer = new ItemSerializer();
                        ItemComm item = serializer.jsonToItem(message.getBody(String.class));
                        onReceivedMessage(item);
                    } catch (JMSException ex) {
                        Logger.getLogger(MessagingGateway.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (JMSException | NamingException ex) {
            Logger.getLogger(MessagingGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public abstract void onReceivedMessage(ItemComm item);

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

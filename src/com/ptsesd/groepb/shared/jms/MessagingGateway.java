/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptsesd.groepb.shared.jms;

import com.ptsesd.groepb.shared.ItemComm;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Class responsible for sending and receiving messages to an endpoint of a
 * channel
 *
 * @author user
 */
public class MessagingGateway {

    private static Queue requestorQueue;
    private static Queue replierQueue;

    /*
     * Connection to JMS
     */
    private Connection connection; // to connect to the JMS
    protected Session session; // session for making messages, producers and consumers

    private MessageProducer producer; // for sending messages
    private MessageConsumer consumer; // for receiving messages

    private JMSConsumer queueConsumer;
//    private final JMSConsumer replierQueueConsumer;

    private JMSProducer jmsProducer;
    private String factoryName = "queueConnectionFactory";
    //private static final String JNDI_CONNECTION_FACTORY = "jms/__defaultConnectionFactory";
    private static final String JNDI_CONNECTION_FACTORY = "queueConnectionFactory";

    private static String destinationName = "";

    /**
     * @param <T> the return type
     * @param retvalClass the returned value's {@link Class}
     * @param jndi the JNDI path to the resource
     * @return the resource at the specified {@code jndi} path
     */
    private static <T> T lookup(Class<T> retvalClass, String jndi) {
        try {
            return retvalClass.cast(InitialContext.doLookup(jndi));
        } catch (NamingException ex) {
            throw new IllegalArgumentException("failed to lookup instance of " + retvalClass + " at " + jndi, ex);
        }
    }

    public MessagingGateway(String destinationName) {
        MessagingGateway.destinationName = destinationName;
        //Context jndiContext = new InitialContext();
        QueueConnectionFactory connectionFactory = (QueueConnectionFactory) lookup(QueueConnectionFactory.class, JNDI_CONNECTION_FACTORY);
        JMSContext jmsContext = connectionFactory.createContext();
        requestorQueue = lookup(Queue.class, destinationName);
        replierQueue = lookup(Queue.class, "clientReplierQueue");
        jmsProducer = jmsContext.createProducer();
        queueConsumer = jmsContext.createConsumer(requestorQueue);
        //replierQueueConsumer = jmsContext.createConsumer(replierQueue);
        queueConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("inside messaginggateway creating new messagelistener");
                queueConsumer.getMessageListener().onMessage(message);
            }
        });
    }

    public static Destination getDestination(String destinationName) {
        MessagingGateway.destinationName = destinationName;
        Destination requestorDestination = null;
        //requestorDestination = (Destination) lookup(Queue.class, destinationName);
        requestorQueue = lookup(Queue.class, destinationName);
        //return requestorDestination;
        return requestorQueue;
    }

    public void start() {
        try {
            connection.start();
        } catch (JMSException ex) {
            System.err.println(String.format("JMSException in MessagingGateway start() : %s", ex.getMessage()));
        }
    }

    public Message createMsg(String body) {
        Message msg = null;
        try {
            msg = session.createTextMessage(body);
        } catch (JMSException ex) {
            System.err.println(String.format("JMSException in MessagingGateway createMsg() : %s", ex.getMessage()));
        }
        return msg;
    }

    public void setListener(MessageListener l) {
        this.queueConsumer.setMessageListener(l);
    }

    public void send(Message msg, Destination requestorDestination) {
        jmsProducer.send(requestorDestination, msg);
    }

    public void send(String msg, Destination requestorDestination) {
        jmsProducer.send(requestorDestination, msg);
    }

    public void send(boolean result, Destination requestorDestination) {
        jmsProducer.send(requestorDestination, result);
    }

    public void sendItemComm(ItemComm item) {
        Message msg = null;
        String json = ItemSerializer.itemToJson(item);

        this.send(json, requestorQueue);

    }

}

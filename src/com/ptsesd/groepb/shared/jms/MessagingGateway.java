/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptsesd.groepb.shared.jms;

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
 * @author Lisanne
 */
public class MessagingGateway {
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private Destination receiverDestination;

    public MessagingGateway(String factoryName, String senderName, String receiverName) {
        try {
            // connecting to the JMS
            Context jndiContext = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup(factoryName);
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // connect to the sender channel
            Destination senderDestination = (Destination) jndiContext.lookup(senderName); //request
            producer = session.createProducer(senderDestination);
            // connect to the receiver channel
            Destination receiverDestination = (Destination) jndiContext.lookup(receiverName);//reply
            consumer = session.createConsumer(receiverDestination);
        } catch (NamingException ex) {
            Logger.getLogger(MessagingGateway.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMSException ex) {
            Logger.getLogger(MessagingGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public MessagingGateway(String replyReceiverQueue, String requestReceiverQueue) {
        try {
            // connecting to the JMS
            final String factoryName = "queueConnectionFactory";//queueNames.get(JMSSettings.CONNECTION);

            Context jndiContext = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup(factoryName);
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // connect to the sender channel
            if (replyReceiverQueue != null) {
                Destination senderDestination = (Destination) jndiContext.lookup(replyReceiverQueue); //request
                producer = session.createProducer(senderDestination);
            }
            // connect to the receiver channel
            if (requestReceiverQueue != null) {
                receiverDestination = (Destination) jndiContext.lookup(requestReceiverQueue);//reply
                consumer = session.createConsumer(receiverDestination);
            }
        } catch (NamingException ex) {
            Logger.getLogger(MessagingGateway.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMSException ex) {
            Logger.getLogger(MessagingGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Destination getReceiverDestination() {
        return receiverDestination;
    }

    public Message createMessage(String body) {
        try {
            return session.createTextMessage(body);
        } catch (JMSException ex) {
            Logger.getLogger(MessagingGateway.class
                    .getName()).log(Level.SEVERE, null, ex);

            return null;
        }
    }

    public void sendMessage(Message msg) {
        try {
            producer.send(msg);
        } catch (JMSException ex) {
            Logger.getLogger(MessagingGateway.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setMessageListener(MessageListener listener) {
        try {
            consumer.setMessageListener(listener);
        } catch (JMSException ex) {
            Logger.getLogger(MessagingGateway.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openConnection() {
        try {
            connection.start();

        } catch (JMSException ex) {
            Logger.getLogger(MessagingGateway.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessage(Destination dest, Message replymessage) {
        try {
            MessageProducer unidentified = session.createProducer(null);
            unidentified.send(dest, replymessage);
        } catch (JMSException ex) {
            Logger.getLogger(MessagingGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

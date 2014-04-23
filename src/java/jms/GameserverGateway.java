/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jms;

import com.google.gson.Gson;
import com.ptsesd.groepb.shared.jms.MessagingGateway;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Toon
 */
public abstract class GameserverGateway {

    private final Gson gson = new Gson();
    private final MessagingGateway messagingGateway;
    private static final String JNDI_QUEUE = "gameServerReplierQueue";

    public GameserverGateway() {
        this.messagingGateway = new MessagingGateway(JNDI_QUEUE);
        this.messagingGateway.setListener(new MessageListener() {

            @Override
            public void onMessage(Message msg) {
                try {
                    System.out.println("GOT MESSAGE OMG " + msg.getBody(String.class));

                    messagingGateway.send(processRequest(msg), MessagingGateway.getDestination(JNDI_QUEUE));
                } catch (JMSException ex) {
                    Logger.getLogger(GameserverGateway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public abstract boolean processRequest(Message message);
}

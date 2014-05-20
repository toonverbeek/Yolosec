/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptsesd.groepb.shared.jms;

import com.ptsesd.groepb.shared.jms.MessagingGateway;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Toon
 */
public abstract class EconomyServerGateway {

    private MessagingGateway messagingGateway;
    private String JNDI_QUEUE_REQUEST = "gameServerRequestQueue";
    private String JNDI_QUEUE_REPLY = "gameServerReplierQueue";

    public EconomyServerGateway() {
        messagingGateway = new MessagingGateway(JNDI_QUEUE_REPLY,JNDI_QUEUE_REQUEST );
        messagingGateway.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message msg) {
                processRequest(msg);
            }
        });
    }


    public abstract boolean processRequest(Message message);
}

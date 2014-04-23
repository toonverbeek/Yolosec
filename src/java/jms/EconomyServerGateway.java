/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jms;

import com.google.gson.Gson;
import com.ptsesd.groepb.shared.jms.MessagingGateway;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Toon
 */
public abstract class EconomyServerGateway {

    private MessagingGateway messagingGateway;
    private Gson gson = new Gson();
    private static String JNDI_QUEUE = "economyRequestorQueue";

    public EconomyServerGateway() {
        messagingGateway = new MessagingGateway(JNDI_QUEUE);
        messagingGateway.setListener(new MessageListener() {

            @Override
            public void onMessage(Message msg) {
                processRequest(msg);
            }
        });
    }


    public abstract boolean processRequest(Message message);
}

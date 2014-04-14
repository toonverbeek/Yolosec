/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.jms;

import com.google.gson.Gson;
import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.jms.ItemSerializer;
import com.ptsesd.groepb.shared.jms.MessagingGateway;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Tim
 */
public abstract class ClientRequestorGateway {

    private MessagingGateway mg;
    private static final String JNDI_QUEUE = "clientRequestorQueue";
    private Gson gson = new Gson();

    public ClientRequestorGateway() {
        mg = new MessagingGateway(JNDI_QUEUE);
        mg.setListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                processReply(message.toString());
            }
        });
    }

    public abstract void processReply(String reply);

    public void sendRequest(String[] payload) {
        String jsonPayload = gson.toJson(payload);
        //Message m = mg.createMsg(jsonPayload);
        mg.send(jsonPayload, MessagingGateway.getDestination(JNDI_QUEUE));
        System.out.println("looks like it worked.");
    }

    public void sendItemComm(ItemComm item) {
        Message msg = null;
        ItemSerializer serializer = new ItemSerializer();
        String json = serializer.itemToJson(item);

        //this.jmsProducer.send(msg);
        mg.send(json, MessagingGateway.getDestination(JNDI_QUEUE));

    }
}

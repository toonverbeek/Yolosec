/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jms;

import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.jms.ItemSerializer;
import com.ptsesd.groepb.shared.jms.MessagingGateway;
import com.ptsesd.groepb.shared.socket.InventoryReply;
import com.ptsesd.groepb.shared.socket.InventoryRequest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author Administrator
 */
public abstract class InventoryGateway {

    private final MessagingGateway inventory_MessagingGateway;
    private static final String JNDI_INVENTORY_QUEUE_REQUEST = "inventoryRequestorQueue";
    private static final String JNDI_QUEUE_REPLY = "gameServerReplierQueue";

    public InventoryGateway() {
        this.inventory_MessagingGateway = new MessagingGateway(JNDI_QUEUE_REPLY, JNDI_INVENTORY_QUEUE_REQUEST);
        this.inventory_MessagingGateway.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message msg) {
                try {
                    TextMessage tMsg = (TextMessage) msg;
                    InventoryRequest ir = ItemSerializer.jsonToInventoryRequest(tMsg.getText());
                    processRequest(ir);
                } catch (JMSException ex) {
                    Logger.getLogger(InventoryGateway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        inventory_MessagingGateway.openConnection();
    }

    public abstract void processRequest(InventoryRequest request);

    public void sendRefresh(InventoryReply reply, InventoryReply ahReply){
        String inventoryString = Serializer.serializeInventoryReplyAsGamePacktet(reply);
        Message invMessage = inventory_MessagingGateway.createMessage(inventoryString);
        inventory_MessagingGateway.sendMessage(invMessage);
        
        String auctionHouseString = Serializer.serializeInventoryReplyAsGamePacktet(ahReply);
        Message ahMessage = inventory_MessagingGateway.createMessage(auctionHouseString);
        inventory_MessagingGateway.sendMessage(ahMessage);
    }

    public void sendReply(InventoryReply reply) {
        String inventoryString = Serializer.serializeInventoryReplyAsGamePacktet(reply);
        Message invMessage = inventory_MessagingGateway.createMessage(inventoryString);
        inventory_MessagingGateway.sendMessage(invMessage);
    }
}

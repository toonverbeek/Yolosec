/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jms;

import com.google.gson.Gson;
import com.ptsesd.groepb.shared.GamePacket;
import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.jms.ItemSerializer;
import com.ptsesd.groepb.shared.jms.MessagingGateway;
import com.ptsesd.groepb.shared.socket.AuctionReply;
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
 * @author Toon
 */
public abstract class GameserverGateway {

    private final MessagingGateway game_MessagingGateway;
    private static final String JNDI_GAMESERVER_QUEUE_REQUEST = "gameServerRequestorQueue";
    private static final String JNDI_QUEUE_REPLY = "gameServerReplierQueue";

    public GameserverGateway() {
        this.game_MessagingGateway = new MessagingGateway(JNDI_QUEUE_REPLY, JNDI_GAMESERVER_QUEUE_REQUEST);
        this.game_MessagingGateway.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message msg) {
                try {
                    TextMessage tMsg = (TextMessage) msg;
                    ItemComm itemRequest = ItemSerializer.jsonToItem(tMsg.getText());
                    processRequest(itemRequest);
                    
                } catch (JMSException ex) {
                    Logger.getLogger(GameserverGateway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        game_MessagingGateway.openConnection();
    }

    public abstract void processRequest(ItemComm itemRequest);
    
    public void sendReply(ItemComm sendItem){
        Message createMessage = game_MessagingGateway.createMessage(ItemSerializer.itemToJson(sendItem));
        game_MessagingGateway.sendMessage(createMessage);
    }
    
    public void sendRefresh(InventoryReply reply, InventoryReply ahReply){
        String inventoryString = Serializer.serializeInventoryReplyAsGamePacktet(reply);
        Message invMessage = game_MessagingGateway.createMessage(inventoryString);
        game_MessagingGateway.sendMessage(invMessage);
        
        String auctionHouseString = Serializer.serializeInventoryReplyAsGamePacktet(ahReply);
        Message ahMessage = game_MessagingGateway.createMessage(auctionHouseString);
        game_MessagingGateway.sendMessage(ahMessage);
    }
}

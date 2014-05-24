/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jms;

import com.ptsesd.groepb.shared.jms.ItemSerializer;
import com.ptsesd.groepb.shared.jms.MessagingGateway;
import com.ptsesd.groepb.shared.jms.ResourceMessage;
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
public abstract class ResourceGateway {
    private MessagingGateway messagingGateway;
    //Left is JNDI name and Right the physical destination
    private final String RESOURCE_REQUEST_QUEUE = "resourceRequestQueue";
    private final String RESOURCE_REPLY_QUEUE = "resourceReplyQueue";
    
    public ResourceGateway() {
        messagingGateway = new MessagingGateway(RESOURCE_REPLY_QUEUE, RESOURCE_REQUEST_QUEUE);
        messagingGateway.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message msg) {
                try {
                    TextMessage tMsg = (TextMessage) msg;
                    ResourceMessage ir = ItemSerializer.jsonToResourceMessage(tMsg.getText());
                    System.out.println("---[RESOURCEGATEWAY] Received ResourceMessage: " + tMsg.getText());
                    processRequest(ir);
                } catch (JMSException ex) {
                    Logger.getLogger(ResourceGateway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        messagingGateway.openConnection();
    }

    public abstract void processRequest(ResourceMessage resRep);

    public void sendResourceReply(ResourceMessage resReq) {
        System.out.println("---[RESOURCEGATEWAY] Sent ResourceMessage");
        messagingGateway.sendMessage(messagingGateway.createMessage(ItemSerializer.resourceMessageToJson(resReq)));
    }
}

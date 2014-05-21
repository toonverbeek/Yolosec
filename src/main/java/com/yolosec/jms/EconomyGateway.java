package com.yolosec.jms;

import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.jms.ItemSerializer;
import com.ptsesd.groepb.shared.jms.MessagingGateway;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Administrator
 */
public abstract class EconomyGateway {

    private MessagingGateway messagingGateway;
    private String REQUESTOR_JNDI_QUEUE = "gameServerRequestorQueue";
    private String REPLIER_JNDI_QUEUE = "gameServerReplierQueue";

    public EconomyGateway() {
        messagingGateway = new MessagingGateway(REPLIER_JNDI_QUEUE, REQUESTOR_JNDI_QUEUE);
        messagingGateway.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message msg) {
                System.out.println("MESSAGE 1!!");
                processRequest(msg);
            }
        });
        messagingGateway.openConnection();
    }

    public abstract boolean processRequest(Message message);

    public void sendItem(ItemComm item) {
        messagingGateway.sendMessage(messagingGateway.createMessage(ItemSerializer.itemToJson(item)));
        System.out.println("SEND ITEM");
    }
}

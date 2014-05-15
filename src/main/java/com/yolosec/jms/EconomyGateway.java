package com.yolosec.jms;

import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.jms.MessagingGateway;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Administrator
 */
public abstract class EconomyGateway {

    private MessagingGateway messagingGateway;
    private String JNDI_QUEUE = "gameServerRequestorQueue";

    public EconomyGateway() {
        messagingGateway = new MessagingGateway(JNDI_QUEUE);
        messagingGateway.setListener(new MessageListener() {

            @Override
            public void onMessage(Message msg) {
                processRequest(msg);
            }
        });
    }

    public abstract boolean processRequest(Message message);

    public void sendItem(ItemComm item) {
        messagingGateway.sendItemComm(item);
    }
}

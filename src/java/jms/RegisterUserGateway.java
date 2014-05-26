/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jms;

import com.ptsesd.groepb.shared.jms.InsertUserMessage;
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
public class RegisterUserGateway {
    private MessagingGateway messagingGateway;
    //Left is JNDI name and Right the physical destination
    private final String REGUSER_REQUEST_QUEUE = "reguserRequestQueue";
    private final String REGUSER_REPLY_QUEUE = "reguserReplyQueue";
    
    public RegisterUserGateway() {
        messagingGateway = new MessagingGateway(REGUSER_REQUEST_QUEUE, REGUSER_REPLY_QUEUE);
        messagingGateway.openConnection();
    }

    public void sendRegisterRequest(InsertUserMessage resReq) {
        System.out.println("---[REGISTERUSERGATEWAY] Sent Register User Message");
        messagingGateway.sendMessage(messagingGateway.createMessage(ItemSerializer.insertUserMessageToJson(resReq)));
    }
}

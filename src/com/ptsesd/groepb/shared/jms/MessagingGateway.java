/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared.jms;

import javax.jms.Message;

/**
 *
 * @author Tim
 */
public abstract class MessagingGateway {
    
    private static String factoryName = "connectionFactory";
    
    public MessagingGateway(String requestQueue, String replyQueue) {
        
    }
    
    public abstract void onReceivedMessage(Message msg);
    
    private void sendMessage() {
        
    }
     
}

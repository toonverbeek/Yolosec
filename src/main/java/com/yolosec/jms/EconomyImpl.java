package com.yolosec.jms;

import com.ptsesd.groepb.shared.ItemComm;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;

/**
 *
 * @author Administrator
 */
public class EconomyImpl {

    EconomyGateway gtw;

    public EconomyImpl() {
        gtw = new EconomyGateway() {

            @Override
            public boolean processRequest(Message message) {
                try {
                    System.out.println("MESSAGE!!" + message.getBody(String.class));
                    return true;
                } catch (JMSException ex) {
                    Logger.getLogger(EconomyImpl.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
            }
        };
    }

    public void processItem(int spaceshipId, ItemComm item) {
        gtw.sendItem(item);
    }
}

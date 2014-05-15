package com.yolosec.jms;

import com.ptsesd.groepb.shared.ItemComm;
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
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    public void processItem(int spaceshipId, ItemComm item) {
        gtw.sendItem(item);
    }
}

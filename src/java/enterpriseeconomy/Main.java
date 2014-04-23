/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enterpriseeconomy;

import com.ptsesd.groepb.shared.AuctionHouseRequestType;
import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.jms.ItemSerializer;
import com.ptsesd.groepb.shared.jms.MessagingGateway;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import jms.EconomyServerGateway;
import service.EconomyService;

/**
 *
 * @author Toon
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EconomyService eService = new EconomyService();
        
        System.out.println("STARTING APPLICATION");
        // TODO code application logic here

        ItemComm itemComm = new ItemComm(1L, 1, 100, "Common", AuctionHouseRequestType.SELL);
        String json = ItemSerializer.itemToJson(itemComm);

        System.out.println("CREATED ITEMCOMM JSON");

        System.out.println("**CREATING MESSAGINGGATEWAY**");
        
        MessagingGateway msg = new MessagingGateway("economyRequestorQueue");
        
        System.out.println("**CREATED MESSAGINGGATEWAY**");
        //mock serverside
        EconomyServerGateway esg = new EconomyServerGateway() {

            @Override
            public boolean processRequest(Message message) {
                try {
                    System.out.println("got message: " + message.getBody(String.class));
                } catch (JMSException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                return true;
            }
        };

        //oh dear
        msg.sendItemComm(itemComm);
        
        
        Runnable r = new Runnable() {

            @Override
            public void run() {
                while (true) {

                }
            }

        };
        Thread t = new Thread(r);
        t.start();
    }
}

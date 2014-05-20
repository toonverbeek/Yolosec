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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
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
        while (true) {
            try {
                System.out.println("Enter command: ");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String input = br.readLine();
                if (input.equals("s")) {
                    System.out.println("STARTING APPLICATION!");
                    EconomyService eService = new EconomyService();
                }
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("STARTING APPLICATION");
            // TODO code application logic here

        }
    }
}

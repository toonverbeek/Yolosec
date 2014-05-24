/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enterpriseeconomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.ws.Endpoint;
import service.EconomyService;
import webservice.StoreWebservice;

/**
 *
 * @author Toon
 */
public class Main {

    private static final String url = "http://192.168.24.11/StoreWebservice";
    private static final String PERSISTENCE_UNIT_NAME = "EnterpriseEconomyPU";
    private static EntityManagerFactory factory;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        EntityManager em;

        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
        while (true) {
            try {
                System.out.println("Enter command: ");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String input = br.readLine();
                if (input.equals("s")) {
                    System.out.println("---[MAIN] STARTING APPLICATION");
                    EconomyService eService = new EconomyService(em);
                    eService.startEconomyServervice();
                } else if (input.equals("w")) {
                    System.out.println("---[MAIN] STARTING WEBSERVICE");
                    System.out.println(String.format("---[MAIN] WEBSERVICE URL %s", url));
                    Endpoint.publish(url, new StoreWebservice(em));
                }
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("STARTING APPLICATION");
            // TODO code application logic here

        }
    }
}

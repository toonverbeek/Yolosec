/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package economysystem;

import service.EconomyService;

/**
 *
 * @author Toon
 */
public class EconomySystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        EconomyService e = new EconomyService();
        
        System.out.println(e.getAllItems().size() + " items in database");
        
        
        
        //Close EntityManager when finished running:
        e.closeEntityManager();
    }

}

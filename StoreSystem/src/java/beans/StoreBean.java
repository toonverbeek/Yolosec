/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import javax.inject.Inject;
import service.StoreService;

/**
 *
 * @author Lisanne
 */
public class StoreBean {
    @Inject
    private StoreService storeService;
    
    public StoreBean(){
        
    }
}

package com.server;

import com.modules.ClientConnectionModule;
import com.modules.PlayerLocationModule;

/**
 *
 * @author Peter
 */
public class LoadModules {

    public static void main(String[] args) throws Exception {
         
        DbConnector.readDataBase();
        PlayerLocationModule playerLocationModule = new PlayerLocationModule();
        playerLocationModule.run();
        
    }
}

package com.server;

import com.modules.ClientConnectionModule;

/**
 *
 * @author Peter
 */
public class LoadModules {

    public static void main(String[] args) throws Exception {
         
        DbConnector.readDataBase();
        ClientConnectionModule serverModule = new ClientConnectionModule();
        serverModule.run();
    }
}

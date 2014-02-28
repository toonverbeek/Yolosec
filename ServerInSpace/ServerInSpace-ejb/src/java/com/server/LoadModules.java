package com.server;

import com.console.ConsoleApp;
import com.modules.ClientBroadcastModule;
import com.modules.ClientConnectionModule;

/**
 *
 * @author Peter
 */
public class LoadModules {

    public static void main(String[] args) throws Exception {
         
        DbConnector.readDataBase();
        
        ClientConnectionModule serverModule = new ClientConnectionModule();
        Thread receiverThread = new Thread(serverModule);
        
        receiverThread.start();
        
        ClientBroadcastModule broadcastModule = new ClientBroadcastModule(serverModule);
        Thread broadcastThread = new Thread(broadcastModule);
        
        broadcastThread.start();
        
        ConsoleApp consoleApp = new ConsoleApp(serverModule);
        Thread consoleThread = new Thread(consoleApp);
        
        consoleThread.start();
    }
}

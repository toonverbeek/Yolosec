package com.server;

import com.console.ConsoleApp;
import com.modules.player.ClientBroadcastModule;

/**
 *
 * @author Peter
 */
public class LoadModules {

    public static void main(String[] args) throws Exception {
         
        DbConnector.readDataBase();
        //Create broadcast module
        ClientBroadcastModule broadcastModule = new ClientBroadcastModule();
        Thread broadcastThread = new Thread(broadcastModule);
        //Create server module
        ConnectionServer serverModule = new ConnectionServer(broadcastModule);
        Thread receiverThread = new Thread(serverModule);
        
        broadcastModule.setServerModule(serverModule);
        
        receiverThread.start();
        broadcastThread.start();
        
        ConsoleApp consoleApp = new ConsoleApp(serverModule);
        Thread consoleThread = new Thread(consoleApp);
        
        consoleThread.start();
    }
}

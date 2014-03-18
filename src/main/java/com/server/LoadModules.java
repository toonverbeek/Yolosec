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
        
        //set the servermodule
        broadcastModule.setServerModule(serverModule);
        
        //start the threads
        receiverThread.start();
        broadcastThread.start();
        
        //Create the console application
        ConsoleApp consoleApp = new ConsoleApp(serverModule);
        Thread consoleThread = new Thread(consoleApp);
        
        //Run the console application
        consoleThread.start();
    }
}

package com.yolosec.launcher;

import com.yolosec.service.GameService;
import com.yolosec.util.ConsoleApp;
import com.yolosec.service.GameBroadcastService;

/**
 *
 * @author Peter
 */
public class LoadModules {

    public static void main(String[] args) throws Exception {
         
        //Create broadcast module
        GameBroadcastService broadcastModule = new GameBroadcastService();
        Thread broadcastThread = new Thread(broadcastModule);
        
        //Create server module
        GameService serverModule = new GameService(broadcastModule);
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

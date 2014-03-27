
package com.modules.thread;

import com.google.gson.Gson;
import com.server.ConnectionServer;

/**
 *
 * @author user
 */
public class ClientBroadcastThread implements Runnable{
    
    private boolean isRunning = false;
    private ConnectionServer serverModule = null;
    
    public ClientBroadcastThread(){
        this.isRunning = true;
    }
    
    /**
     * Set server module to retrieve information for broadcasting
     * 
     * @param server 
     */
    public void setServerModule(ConnectionServer server){
        this.serverModule = server;
    }
    
    /**
     * The broadcast module start broadcasting player location to the current connected players
     */
    @Override
    public synchronized void run() {
        while (isRunning) {
            try {
                if(this.serverModule != null){
                    serverModule.broadcastPositions();
                }
                this.wait(16);
            } catch (InterruptedException ex) {
                System.out.println(String.format("---[BROADCAST] ClientBroadcastModule is interrupted : %s", ex.getMessage()));
            }
        }
    }
    
}

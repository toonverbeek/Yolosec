
package com.modules.player;

import com.server.ClientConnection;
import com.server.ConnectionServer;
import java.util.List;
import com.ptsesd.groepb.shared.AsteroidComm;

/**
 *
 * @author user
 */
public class ClientBroadcastModule implements Runnable{
    
    private boolean isRunning = false;
    private ConnectionServer serverModule = null;
    
    public ClientBroadcastModule(){
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
                System.out.println(String.format("ClientBroadcastModule is interrupted : %s", ex.getMessage()));
            }
        }
    }
    
    /**
     * broadcast the current asteroids
     */
    public void broadcastAsteroids() {
        List<ClientConnection> listeners = serverModule.getClientConnections();
        List<AsteroidComm> asteroids = serverModule.getAsteroids();
        
        for(ClientConnection c : listeners){
            
        }
    }
}

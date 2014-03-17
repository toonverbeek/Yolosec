
package com.modules.player;

import com.server.ClientConnection;
import com.server.ConnectionServer;
import java.util.List;
import shared.Asteroid;

/**
 *
 * @author user
 */
public class ClientBroadcastModule implements Runnable{
    
    private boolean isRunning = false;
    private ConnectionServer connectionModule = null;
    
    public ClientBroadcastModule(){
        this.isRunning = true;
    }
    
    public void setServerModule(ConnectionServer server){
        this.connectionModule = server;
    }
    
    @Override
    public synchronized void run() {
        while (isRunning) {
            try {
                if(this.connectionModule != null){
                    connectionModule.broadcastPositions();
                }
                this.wait(16);
            } catch (InterruptedException ex) {
                System.out.println(String.format("ClientBroadcastModule is interrupted : %s", ex.getMessage()));
            }
        }
    }
    
    public void broadcastAsteroids() {
        List<ClientConnection> listeners = connectionModule.getClientConnections();
        List<Asteroid> asteroids = connectionModule.getAsteroids();
        
        for(ClientConnection c : listeners){
            
        }
    }
}

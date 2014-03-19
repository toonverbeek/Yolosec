
package com.modules.player;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.server.ClientConnection;
import com.server.ConnectionServer;
import java.util.List;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.AsteroidType;
import com.ptsesd.groepb.shared.GamePacket;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.SpaceshipComm;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class ClientBroadcastModule implements Runnable{
    
    private boolean isRunning = false;
    private ConnectionServer serverModule = null;
    private final Gson gson;
    
    public ClientBroadcastModule(){

        this.isRunning = true;
        gson = new Gson();
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
   
        for (ClientConnection c : listeners) {
            for (AsteroidComm asc : asteroids) {
                PrintWriter writer = null;
                try {
                    String asteroidString = Serializer.serializeAsteroidAsGamePacket(asc.getHeader(), asc.getType(), asc.getResourceAmount(), (int) asc.getX(), (int) asc.getY());
                    writer = new PrintWriter(c.getSocket().getOutputStream(), true);
                    
                    Type com = new TypeToken<List<GamePacket>>() {
                    }.getType();
                    
                    String json = gson.toJson(asteroidString, com);
                    //send the object
                    writer.println(json);
                } catch (IOException ex) {
                    Logger.getLogger(ClientBroadcastModule.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
        }
    }
}

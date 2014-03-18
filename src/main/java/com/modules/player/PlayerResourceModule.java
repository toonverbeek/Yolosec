
package com.modules.player;

import com.server.ClientConnection;
import com.server.DbConnector;
import java.util.Map;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.SpaceshipComm;

/**
 *
 * @author user
 */
public class PlayerResourceModule {
    
    public PlayerResourceModule(){
    }
    
    public void recievedAsteroid(AsteroidComm asteroid, int clientID){
        DbConnector.setSpaceshipResourceAmount(clientID, asteroid.getResourceAmount());
    }
}

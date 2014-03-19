
package com.modules.player;

import com.server.ClientConnection;
import com.server.DbConnector;
import java.util.Map;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.SpaceshipComm;
import java.util.List;

/**
 *
 * @author user
 */
public class PlayerResourceModule {
    
    private List<AsteroidComm> asteroids;
    
    public PlayerResourceModule(List<AsteroidComm> asteroids){
        this.asteroids = asteroids;
    }
    
    public void recievedAsteroid(AsteroidComm asteroid, int clientID){
        
        for(AsteroidComm ast : asteroids){
            if(ast.getX() == asteroid.getX() && ast.getY() == asteroid.getY()){
                ast.setResourceAmount(asteroid.getResourceAmount());
                break;
            }
        }
        
        DbConnector.setSpaceshipResourceAmount(clientID, asteroid.getResourceAmount());
    }

    public List<AsteroidComm> getAsteroids() {
        return this.asteroids;
    }
}

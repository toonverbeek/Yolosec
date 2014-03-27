
package com.modules.player;

import com.server.ClientConnection;
import com.server.DbConnector;
import java.util.Map;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.SpaceshipComm;
import com.server.ConnectionServer;
import java.util.List;

/**
 *
 * @author user
 */
public class AsteroidCommModule {
    
    private List<AsteroidComm> asteroids;
    
    public AsteroidCommModule(List<AsteroidComm> asteroids){
        this.asteroids = asteroids;
        if (ConnectionServer.debug) {
            for (AsteroidComm ast : asteroids) {
                System.out.println(String.format("---[ASTEROID} NEW X { %s } Y { %s } RESOURCE AMOUNT { %s } TYPE { %s }", ast.getX(), ast.getY(), ast.getResourceAmount(), ast.getType()));
            }
        }
    }
    
    public void setAsteroids(List<AsteroidComm> asteroids){
        this.asteroids = asteroids;
        if (ConnectionServer.debug) {
            for (AsteroidComm ast : asteroids) {
                System.out.println(String.format("---[ASTEROID] NEW - X { %s } Y { %s } RESOURCE AMOUNT { %s } TYPE { %s }", ast.getX(), ast.getY(), ast.getResourceAmount(), ast.getType()));
            }
        }
    }
    
    /**
     * Return the current list of AsteroidComms
     * @return List<AsteroidComm>
     */
    public List<AsteroidComm> getAsteroids() {
        return this.asteroids;
    }
    
    /**
     * Update the Asteroid in the current Asteroids list
     * @param asteroid
     */
    public void recievedAsteroid(AsteroidComm asteroid){
        for(AsteroidComm ast : asteroids){
            if(ast.getX() == asteroid.getX() && ast.getY() == asteroid.getY()){
                ast.setResourceAmount(asteroid.getResourceAmount());
                if(ConnectionServer.debug){
                    System.out.println(String.format("---[ASTEROID] X1 [ %s ] X2 [ %s ] Y1 [ %s ] Y2 [ %s ] NEW RESOURCE AMOUNT [ %s ]", ast.getX(), asteroid.getX(), ast.getY(), asteroid.getY(), asteroid.getResourceAmount()));
                    System.out.println(String.format("---[ASTEROID] Setted asteroid resource amount %s \n", asteroid.getResourceAmount()));
                }
                break;
            }
        }
    }
}

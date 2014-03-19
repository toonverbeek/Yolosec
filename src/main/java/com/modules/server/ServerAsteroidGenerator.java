
package com.modules.server;

import com.server.ClientConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.AsteroidType;

/**
 *
 * @author user
 */
public class ServerAsteroidGenerator {
    
    private final int mapSizeX = 16000;
    private final int mapSizeY = 16000;
    
    private int[][] map;
    private List<position> occupiedPositions;
    
    private final int amountOfAsteroids = 25;
    
    public ServerAsteroidGenerator(){
        map = new int[1600][1600];
        occupiedPositions = new ArrayList<position>();
    }
    
    public List<AsteroidComm> generateAsteroids(){
        List<AsteroidComm> _asteroids = new ArrayList<>();
        
        while (_asteroids.size() < amountOfAsteroids) {
            Random r = new Random();
            //Remove 20 units from total range and add 10 to it
            //This way the asteroids cannot spawn 10 unit (==100pixels) from the map borders
            int rMapUnitX = r.nextInt(1580);
            rMapUnitX += 10;
            
            int rMapUnitY = r.nextInt(1580);
            rMapUnitY += 10;
            
            //#TODO: Determine asteroid type
            
            //Create random resource amount 
            int rResourceAmount = 100;
            
            //Check if is colliding with current rendered asteroids
            if(!isColliding(rMapUnitX, rMapUnitY, rResourceAmount, _asteroids) || _asteroids.isEmpty()){
                float rmapX = rMapUnitX * 10;
                float rmapY = rMapUnitY * 10;
                _asteroids.add(new AsteroidComm(AsteroidComm.class.getSimpleName(),AsteroidType.common, rResourceAmount, rmapX, rmapY));
            }
        }
        
        return _asteroids;
    }
    
    private boolean isColliding(int posUnitX, int posUnitY, int resourceAmount, Collection<AsteroidComm> checkAsteroids){
        boolean isColliding = false;
        int newAstSize = calculateSizeFromResource(resourceAmount);
        position newAsteroidPos = new position(posUnitX, posUnitY, newAstSize);
        for(AsteroidComm asteroid : checkAsteroids){
            //get the position parameters
            int astPosX = (int) asteroid.getX();
            int astPosY = (int) asteroid.getY();
            int size = calculateSizeFromResource(asteroid.getResourceAmount());
            //create the position from the exisitng asteroid
            position p = new position(astPosX, astPosY, size);
            
            //check if the positions are colliding
            if(positionsColliding(newAsteroidPos, p)){
                //if they are colliding return
                isColliding = true;
                return isColliding;
            }
        }
        
        return isColliding;
    }
    
    public boolean positionsColliding(position a, position b){
        boolean posColliding = false;
        
        int aposX2 = a.posX1 + a.size;
        int aposY2 = a.posY1 + a.size;
        
        int bposX2 = b.posX1 + a.size;
        int bposY2 = b.posY1 + a.size;
        
        //if point bX1 is between point aX1 and point aX2
        if(b.posX1 >= a.posX1 && b.posX1 <= aposX2){
            //if point bY1 is between point aY1 and point aY2
            if(b.posY1 >= a.posY1 && b.posY1 <= aposY2){
                posColliding = true;
            }
            
            //if point bY2 is between point aY1 and point aY2
            if(bposY2 >= a.posY1 && bposY2 <= aposY2){
                posColliding = true;
            }
            
            //if point bY1 is left of aY1 and bY2 is right of aY2
            if(b.posY1 <= a.posY1 && bposY2 >= aposY2){
                posColliding = true;
            }
        }
        
        //if point bX2 is between point aX1 and point aX2
        if(bposX2 >= a.posX1 && bposX2 <= aposX2){
            //if point bY1 is between point aY1 and point aY2
            if(b.posY1 >= a.posY1 && b.posY1 <= aposY2){
                posColliding = true;
            }
            
            //if point bY2 is between point aY1 and point aY2
            if(bposY2 >= a.posY1 && bposY2 <= aposY2){
                posColliding = true;
            }
            
            //if point bY1 is left of aY1 and bY2 is right of aY2
            if(b.posY1 <= a.posY1 && bposY2 >= aposY2){
                posColliding = true;
            }
        }
        
        //if point bX1 is left of point aX1 and point bX2 is right of point aX2
        if(b.posX1 <= a.posX1 && bposX2 >= aposX2){
            //if point bY1 is between point aY1 and point aY2
            if(b.posY1 >= a.posY1 && b.posY1 <= aposY2){
                posColliding = true;
            }
            
            //if point bY2 is between point aY1 and point aY2
            if(bposY2 >= a.posY1 && bposY2 <= aposY2){
                posColliding = true;
            }
            
            //if point bY1 is left of aY1 and bY2 is right of aY2
            if(b.posY1 <= a.posY1 && bposY2 >= aposY2){
                posColliding = true;
            }
        }
        
        return posColliding;
    }
    
    private int calculateSizeFromResource(int resourceAmount){
        return resourceAmount;
    }

    
    
}

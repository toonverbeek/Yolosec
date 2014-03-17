/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

/**
 *
 * @author user
 */
public class AsteroidComm extends GamePacket {
    
    private AsteroidType type;
    private int resourceAmount;
    private float x,y;

    public AsteroidComm(AsteroidType type, int resourceAmount, float x, float y) {
        this.type = type;
        this.resourceAmount = resourceAmount;
        this.x = x;
        this.y = y;
    }

    public AsteroidType getType() {
        return type;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }   
}

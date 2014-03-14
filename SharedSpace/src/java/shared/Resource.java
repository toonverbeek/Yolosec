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

public class Resource {    
    
    private ResourceType type;
    private int resourceAmount;
    private float x,y;

    public Resource(ResourceType type, int resourceAmount, float x, float y) {
        this.type = type;
        this.resourceAmount = resourceAmount;
        this.x = x;
        this.y = y;
    }

    public ResourceType getType() {
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

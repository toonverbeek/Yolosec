
package com.objects;

/**
 *
 * @author user
 */
public class Spaceship {
    
    private int id;

    private float x, y;
    private int direction;

    public Spaceship(int id, float x, float y, int direction) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
    
    public int getId(){
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }
 
    public void setX(float x) {
        this.x = x;
    }
 
    public float getY() {
        return y;
    }
 
    public void setY(float y) {
        this.y = y;
    }
 
    public int getDirection() {
        return this.direction;
    }
 
    public void setDirection(int direction) {
        this.direction = direction;
    }
    
    public void update(float x, float y, int direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
    
}

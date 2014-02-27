
package com.objects;

/**
 *
 * @author user
 */
public class Spaceship {
    
    private int id;

    private double x, y;
    private int direction;

    public Spaceship(int id, double x, double y, int direction) {
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

    public double getX() {
        return x;
    }
 
    public void setX(double x) {
        this.x = x;
    }
 
    public double getY() {
        return y;
    }
 
    public void setY(double y) {
        this.y = y;
    }
 
    public int getDirection() {
        return this.direction;
    }
 
    public void setDirection(int direction) {
        this.direction = direction;
    }
    
    public void update(double x, double y, int direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
    
}

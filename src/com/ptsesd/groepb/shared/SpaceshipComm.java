/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptsesd.groepb.shared;;

/**
 *
 * @author Tim
 */
public class SpaceshipComm extends GamePacket {

    private float x, y;
    private int d, id;
    private final int[] resources;
    private boolean mining;
    public int[] getResources() {
        return resources;
    }

    public SpaceshipComm(String header, Integer id, float x, float y, int direction, int[] resources, boolean mining) {
        super(header);
        this.resources = resources;
        this.x = x;
        this.y = y;
        this.d = direction;
        this.id = id;
        this.mining = mining;
    }

    public int getD() {
        return d;
    }

    public boolean isMining() {
        return mining;
    }
    
    public int getId(){
        return id;
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
        return d;
    }

    public void setDirection(int direction) {
        this.d = direction;
    }
}

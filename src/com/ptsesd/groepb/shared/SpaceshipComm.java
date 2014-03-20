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
    private int d;
    private final int[] resources;

    public int[] getResources() {
        return resources;
    }

    public SpaceshipComm(String header, Integer id, float x, float y, int direction, int[] resources) {
        super(header, id);
        this.resources = resources;
        this.x = x;
        this.y = y;
        this.d = direction;
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

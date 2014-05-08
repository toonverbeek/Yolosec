/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared;

/**
 *
 * @author Tim
 */
public class PlanetComm extends GamePacket {
    private String planetName;
    private int size;
    private float x, y;

    public PlanetComm(String header, String planetName, int size, float x, float y) {
        super(header);
        this.planetName = planetName;
        this.size = size;
        this.x = x;
        this.y = y;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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
}

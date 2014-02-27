/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.communication;

import spaceclient.game.Spaceship;

/**
 *
 * @author Tim
 */
public class SpaceshipComm extends GamePacket{

    private float x, y;
    private int d, id;

    public SpaceshipComm(String header, Spaceship spaceship) {
        super(header);
        this.x = spaceship.getPosition().x;
        this.y = spaceship.getPosition().y;
        this.d = 0;
        this.id = spaceship.getId();

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

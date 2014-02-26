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
public class SpaceshipComm {

    private float x, y;
    private int d;

    public SpaceshipComm(Spaceship spaceship) {

        this.x = spaceship.getPosition().x;
        this.y = spaceship.getPosition().y;
        this.d = 0;

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

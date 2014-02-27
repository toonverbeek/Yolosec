/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;


/**
 *
 * @author Tim
 */
public class SpaceshipComm extends GamePacket{

    private float x, y;
    private int d, id;

    public SpaceshipComm(String header, float x, float y, int d, int id) {
        super(header);
        this.x = x;
        this.y = y;
        this.d = d;
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
        return d;
    }

    public void setDirection(int direction) {
        this.d = direction;
    }
}

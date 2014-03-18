/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.game;

import java.awt.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import shared.AsteroidComm;
import shared.AsteroidType;
import shared.GameObject;
import spaceclient.dao.interfaces.DrawableComponent;

/**
 *
 * @author Toon
 */
public class Asteroid extends GameObject implements DrawableComponent {

    private float x, y;
    private int resourceAmount;
    private AsteroidType type;
    private Rectangle asteroidBounding;
    Shape astroidCircle;

    public Asteroid(AsteroidComm fromPacket) {
        this.x = fromPacket.getX();
        this.y = fromPacket.getX();
        this.resourceAmount = fromPacket.getResourceAmount();
        astroidCircle = new Circle(x, y, resourceAmount);

        this.asteroidBounding = new Rectangle((int) astroidCircle.getX(), (int) astroidCircle.getY(), resourceAmount, resourceAmount);
        this.x = astroidCircle.getX();
        this.y = astroidCircle.getY();
        this.type = fromPacket.getType();

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }

    public AsteroidType getType() {
        return type;
    }

    public Asteroid(float x, float y, int resourceAmount, AsteroidType type) {
        this.x = x;
        this.y = y;
        this.resourceAmount = resourceAmount;
        astroidCircle = new Circle(x, y, resourceAmount);
        this.x = astroidCircle.getX();
        this.y = astroidCircle.getY();
        this.asteroidBounding = new Rectangle((int) astroidCircle.getX(), (int) astroidCircle.getY(), resourceAmount, resourceAmount);

        this.type = type;

    }

    @Override
    public void update(GameContainer gc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateAsteroid(GameContainer gc, Spaceship spaceship) {
        //TODO resourceamount to formula

        if (this.asteroidBounding.intersects(spaceship.getRectangle())) {
            if (resourceAmount > 0) {
                //callback
                if (spaceship.mine(this.getType(), x + (resourceAmount / 2), y + (resourceAmount / 2))) {
                    resourceAmount -= .000000001;
                }
            }
        }

        //asteroidBounding.width = resourceAmount * 2;
    }

    @Override
    public void render(Graphics g, boolean self) {
        if (this.type == AsteroidType.common) {
            g.setColor(Color.red);
        } else if (this.type == AsteroidType.magic) {
            g.setColor(Color.yellow);
        } else {
            g.setColor(Color.pink);
        }
        g.drawOval(x, y, resourceAmount, resourceAmount);

        //astroidCircle = new Circle(x, y, resourceAmount / 2);
        //g.draw(astroidCircle);
//        g.fillOval(x, y, resourceAmount, resourceAmount);
        g.setColor(Color.white);
        g.drawRect(x, y, asteroidBounding.width, asteroidBounding.height);
    }

    @Override
    public Rectangle getRectangle() {
        return this.asteroidBounding;
    }

}

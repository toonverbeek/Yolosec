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

    public Asteroid(AsteroidComm fromPacket) {
        this.x = fromPacket.getX();
        this.y = fromPacket.getX();
        this.resourceAmount = fromPacket.getResourceAmount();
        this.asteroidBounding = new Rectangle((int) x, (int) y, resourceAmount, resourceAmount);
        this.type = fromPacket.getType();
    }

    public Asteroid(float x, float y, int resourceAmount, AsteroidType type) {
        this.x = x;
        this.y = y;
        this.resourceAmount = resourceAmount;
        this.asteroidBounding = new Rectangle((int) x, (int) y, resourceAmount, resourceAmount);
        this.type = type;
    }

    @Override
    public void update(GameContainer gc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateAsteroid(GameContainer gc, Spaceship spaceship) {
        //TODO resourceamount to formula
        if(this.asteroidBounding.intersects(spaceship.getRectangle())) {
            resourceAmount -= .01;            
        }
        asteroidBounding.height = resourceAmount;
        asteroidBounding.width = resourceAmount;
    }

    @Override
    public void render(Graphics g, boolean self) {
        g.setColor(Color.red);
        g.drawOval(x, y, resourceAmount, resourceAmount);
        g.setColor(Color.white);
        g.drawRect(x, y, resourceAmount, resourceAmount);
    }

    @Override
    public Rectangle getRectangle() {
        return this.asteroidBounding;
    }

}

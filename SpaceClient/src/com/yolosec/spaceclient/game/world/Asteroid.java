/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game.world;

import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.AsteroidType;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.SpaceshipComm;
import com.yolosec.spaceclient.communication.Communicator;
import java.awt.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import com.yolosec.spaceclient.dao.interfaces.DrawableComponent;
import com.yolosec.spaceclient.game.player.Spaceship;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Toon
 */
public class Asteroid extends GameObjectImpl implements DrawableComponent {

    private float x, y;
    public int resourceAmount;
    private final AsteroidType type;
    private Rectangle asteroidBounding;
    private Shape astroidCircle;
    public int maxResourceAmount;
    private AngelCodeFont resourceFont;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setResourceAmount(int resourceAmount) {
        this.resourceAmount = resourceAmount;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }

    public AsteroidType getType() {
        return type;
    }

    @Override
    public Rectangle getRectangle() {
        return this.asteroidBounding;
    }

    public void setResourceFont(AngelCodeFont font) {
        this.resourceFont = font;
    }

    public Asteroid(AsteroidComm fromPacket) {
        this.x = fromPacket.getX();
        this.y = fromPacket.getY();
        this.resourceAmount = fromPacket.getResourceAmount();
        this.maxResourceAmount = resourceAmount;
        astroidCircle = new Circle(x, y, resourceAmount);

        this.asteroidBounding = new Rectangle((int) astroidCircle.getX(), (int) astroidCircle.getY(), resourceAmount, resourceAmount);
        this.x = astroidCircle.getX();
        this.y = astroidCircle.getY();
        this.type = fromPacket.getType();

    }

    public Asteroid(float x, float y, int resourceAmount, AsteroidType type) {
        this.x = x;
        this.y = y;
        this.resourceAmount = resourceAmount;
        this.maxResourceAmount = resourceAmount;
        astroidCircle = new Circle(x, y, resourceAmount);
        this.x = astroidCircle.getX();
        this.y = astroidCircle.getY();
        this.asteroidBounding = new Rectangle((int) astroidCircle.getX(), (int) astroidCircle.getY(), resourceAmount, resourceAmount);

        this.type = type;

    }

    @Override
    public void update(GameContainer gc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateAsteroid(GameContainer gc, Spaceship spaceship) {
        //TODO resourceamount to formula

        if (this.asteroidBounding.intersects(spaceship.getRectangle())) {
            if (resourceAmount > 5) {
                //callback
                //if player is mining (i.e. pressing spacebar)
                if (spaceship.mine(this.getType(), this)) {
                    
                }
            } else {
                spaceship.stopMining();

            }
        } else {
            spaceship.stopMining();
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
        g.drawOval(x + ((maxResourceAmount - resourceAmount) / 2), y + ((maxResourceAmount - resourceAmount) / 2), resourceAmount, resourceAmount);
        g.setColor(Color.white);
        g.drawRect(x, y, asteroidBounding.width, asteroidBounding.height);
        resourceFont.drawString(x, y, String.valueOf(resourceAmount));
    }
}

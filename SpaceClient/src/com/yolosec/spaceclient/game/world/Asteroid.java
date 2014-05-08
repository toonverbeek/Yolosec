/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game.world;

import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.AsteroidType;
import java.awt.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import com.yolosec.spaceclient.dao.interfaces.DrawableComponent;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.gui.SpaceClient;
import org.newdawn.slick.AngelCodeFont;

/**
 *
 * @author Toon
 */
public class Asteroid extends GameObjectImpl implements DrawableComponent {

    private float xPosition, yPosition;
    public int resourceAmount;
    private final AsteroidType type;
    private Rectangle asteroidBounding;
    private Shape astroidCircle;
    public int maxResourceAmount;
    private AngelCodeFont resourceFont;

    /**
     *
     * @return the x Position of the asteroid.
     */
    public float getXPosition() {
        return xPosition;
    }

    /**
     *
     * @return the y Position of the asteroid.
     */
    public float getYPosition() {
        return yPosition;
    }

    /**
     * Sets the amount of resources this asteroid can hold.
     *
     * @param resourceAmount the new amount of resources this asteroid will
     * contain.
     */
    public void setResourceAmount(int resourceAmount) {
        this.resourceAmount = resourceAmount;
    }

    /**
     *
     * @return the amount of resources currently available in the asteroid.
     */
    public int getResourceAmount() {
        return resourceAmount;
    }

    /**
     *
     * @return the type of the asteroid (Common, Magic, Rare)
     */
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

    /**
     * Creates a new instance of Asteroid. An Asteroid is a game object that
     * allows a player to mine from it. By mining, the player will gain
     * resources while the Asteroid's available resources will slowly get
     * depleted. Asteroids spawn randomly and it's positions are obtained from
     * the gameserver.
     *
     * @param fromPacket the packet that was received from the gameserver from
     * which the asteroid will be constructed.
     */
    public Asteroid(AsteroidComm fromPacket) {
        this.xPosition = fromPacket.getX();
        this.yPosition = fromPacket.getY();
        this.resourceAmount = fromPacket.getResourceAmount();
        this.maxResourceAmount = resourceAmount;
        astroidCircle = new Circle(xPosition, yPosition, resourceAmount);

        this.asteroidBounding = new Rectangle((int) astroidCircle.getX(), (int) astroidCircle.getY(), resourceAmount, resourceAmount);
        this.type = fromPacket.getType();

    }

    /**
     * Creates a new instance of Asteroid. An Asteroid is a game object that
     * allows a player to mine from it. By mining, the player will gain
     * resources while the Asteroid's available resources will slowly get
     * depleted. Asteroids spawn randomly and it's positions are usually
     * obtained from the gameserver.
     *
     * @param x the x-position to spawn the asteroid at.
     * @param y the y-position to spawn the asteroid at.
     * @param resourceAmount the amount of resources this Asteroid should
     * initially hold.
     * @param type defines the type of this Asteroid (Common, Magic, Rare)
     */
    public Asteroid(float x, float y, int resourceAmount, AsteroidType type) {
        this.xPosition = x;
        this.yPosition = y;
        this.resourceAmount = resourceAmount;
        this.maxResourceAmount = resourceAmount;
        astroidCircle = new Circle(x, y, resourceAmount);
        this.xPosition = astroidCircle.getX();
        this.yPosition = astroidCircle.getY();
        this.asteroidBounding = new Rectangle((int) astroidCircle.getX(), (int) astroidCircle.getY(), resourceAmount, resourceAmount);
        this.type = type;

    }

    @Override
    public void update(GameContainer gc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Updates this instance of Asteroid. Checks for player collision, and if
     * colliding, starts depleting the amount of resources this asteroid holds.
     *
     * @param gc the gameContainer which handles drawing logic.
     * @param spaceship the spaceship that is initializing mining.
     */
    public void updateAsteroid(GameContainer gc, Spaceship spaceship) {
        //TODO resourceamount to formula
        boolean toDraw1 = false;
        boolean toDraw2 = false;
        if (xPosition >= Viewport.viewportPos.x && xPosition <= Viewport.viewportPos.x + (SpaceClient.screenWidth - asteroidBounding.width)) {
            toDraw1 = true;
        }
        if (yPosition >= Viewport.viewportPos.y && yPosition <= Viewport.viewportPos.y + (SpaceClient.screenHeight - asteroidBounding.height)) {
            toDraw2 = true;
        }
        if (toDraw1 && toDraw2) {
            int drawPositionX = (int) (xPosition - Viewport.viewportPos.x);
            int drawPositionY = (int) (yPosition - Viewport.viewportPos.y);
            Shape astroidCircleDraw = new Circle(drawPositionX, drawPositionY, resourceAmount);
            Rectangle asteroidBoundingDraw = new Rectangle((int) astroidCircleDraw.getX(), (int) astroidCircleDraw.getY(), resourceAmount, resourceAmount);
            
            
            if (asteroidBoundingDraw.intersects(spaceship.getRectangle())) {
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
//          } else {
//            if (this.asteroidBounding.intersects(spaceship.getRectangle())) {
//                if (resourceAmount > 5) {
//                    //callback
//                    //if player is mining (i.e. pressing spacebar)
//                    if (spaceship.mine(this.getType(), this)) {
//
//                    }
//                } else {
//                    spaceship.stopMining();
//
//                }
//            } else {
//                spaceship.stopMining();
//            }
            }
        }
        //asteroidBounding.width = resourceAmount * 2;
    }

    @Override
    public void render(Graphics g, boolean self) {
        //if the asteroid is in the viewpoint
        boolean toDraw1 = false;
        boolean toDraw2 = false;
        if (xPosition >= Viewport.viewportPos.x && xPosition <= Viewport.viewportPos.x + (SpaceClient.screenWidth - asteroidBounding.width)) {
            toDraw1 = true;
        }
        if (yPosition >= Viewport.viewportPos.y && yPosition <= Viewport.viewportPos.y + (SpaceClient.screenHeight - asteroidBounding.height)) {
            toDraw2 = true;
        }
        if (toDraw1 && toDraw2) {
            int drawPositionX = (int) (xPosition - Viewport.viewportPos.x);
            int drawPositionY = (int) (yPosition - Viewport.viewportPos.y);

            if (this.type == AsteroidType.common) {
                g.setColor(Color.red);
            } else if (this.type == AsteroidType.magic) {
                g.setColor(Color.yellow);
            } else {
                g.setColor(Color.pink);
            }
            int locX = (int) drawPositionX + ((maxResourceAmount - resourceAmount) / 2);
            int locY = (int) drawPositionY + ((maxResourceAmount - resourceAmount) / 2);

            g.drawOval(locX, locY, resourceAmount, resourceAmount);
            g.setColor(Color.white);
            g.drawRect(drawPositionX, drawPositionY, asteroidBounding.width, asteroidBounding.height);
            resourceFont.drawString(drawPositionX, drawPositionY, String.valueOf(resourceAmount));
        }
    }
}

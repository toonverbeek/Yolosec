/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game.player;

import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.AsteroidType;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.SpaceshipComm;
import com.yolosec.spaceclient.communication.Communicator;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
import java.awt.Rectangle;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;
import com.yolosec.spaceclient.dao.interfaces.DrawableComponent;
import com.yolosec.spaceclient.game.world.Asteroid;

/**
 *
 * @author Toon
 */
public class Spaceship extends GameObjectImpl implements DrawableComponent {

    private int id = 3;

    private float[] polygonPoints;
    private Polygon polygon;
    private Vector2f position, relPosition;
    private float speed = 10;
    private Vector2f velocity = new Vector2f(0, 0);
    private Vector2f acceleration = new Vector2f(0, 0);
    private float horizontalAcceleration = 0;
    private Direction prevDirection;
    private float resistance = .001f;
    private int width, height;
    private Rectangle boundingRectangle;
    private int direction;

    private Input input;
    private int commonResources = 0, magicResources = 0, rareResources = 0;
    private boolean mining = false;
    private Vector2f miningLasers1, mininglasers2;

    public int[] getResources() {
        return new int[]{commonResources, magicResources, rareResources};
    }

    public Spaceship(int width, int height) {
        this.width = width;
        this.height = height;
        position = new Vector2f(100, 100);

        init();
    }

    public Spaceship(SpaceshipComm fromPacket) {
        this.width = 10;
        this.height = 10;
        fromPacket.getResources();
        fromPacket.getId();
        fromPacket.getDirection();
        position = new Vector2f(fromPacket.getX(), fromPacket.getY());
        init();
    }

    private void init() {
        polygonPoints = new float[8];
        polygonPoints[0] = 0;
        polygonPoints[1] = 0;
        polygonPoints[2] = 10;
        polygonPoints[3] = 0;
        polygonPoints[4] = 0;
        polygonPoints[5] = 10;
        polygonPoints[6] = 10;
        polygonPoints[7] = 10;
        polygon = new Polygon(polygonPoints);
        this.boundingRectangle = new Rectangle((int) position.x, (int) position.y, width, height);
        prevDirection = Direction.NEUTRAL;
    }

    @Override
    public void update(GameContainer gc) {
        this.input = gc.getInput();
        calculateMovement(input);
        boundingRectangle.x = (int) position.x;
        boundingRectangle.y = (int) position.y;

    }

    @Override
    public void render(Graphics g, boolean self) {
//        if (self) {
//            polygon.setLocation(new Vector2f(SpaceClient.screenWidth / 2, SpaceClient.screenHeight / 2));
//        } else {
//            polygon.setLocation(position.x + (SpaceClient.screenWidth /2), position.y + (SpaceClient.screenHeight /2));
//        }
        polygon.setLocation(position.x, position.y);
        g.drawString("X : " + position.x + " Y: " + position.y, 50, 50);
        g.drawString("Accel: " + acceleration, 50, 70);
        g.drawString("Common Resources: " + this.getResources()[0], 50, 90);
        g.drawString("Magic Resources: " + this.getResources()[1], 50, 110);
        g.drawString("Rare Resources: " + this.getResources()[2], 50, 130);
        g.drawRect((int) position.x, (int) position.y, width, height);
        if (mining) {
            g.drawLine(miningLasers1.x, miningLasers1.y, mininglasers2.x, mininglasers2.y);
        }
        g.draw(polygon);
    }

    private void calculateMovement(Input input) {
        if (input.isKeyDown(Input.KEY_LEFT)) {
            leftKey();
        } else if (input.isKeyDown(Input.KEY_UP)) {
            upKey();
        } else if (input.isKeyDown(Input.KEY_RIGHT)) {
            rightKey();
        } else if (input.isKeyDown(Input.KEY_DOWN)) {
            downKey();
        }

        //addResistance();
        setLocation();
    }

    private void setLocation() {
        //set x and y
        position.add(velocity);
    }

    public int getId() {
        return id;
    }

    private void leftKey() {
        acceleration.y = 0;
        acceleration.x -= .0015;
        if (acceleration.x < -.01) {
            acceleration.x = -.01f;
        }
        velocity.x += acceleration.x;
    }

    private void rightKey() {
        acceleration.y = 0;
        acceleration.x += .0015;
        if (acceleration.x > .01) {
            acceleration.x = .01f;
        }

        velocity.x += acceleration.x;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    private void upKey() {
        acceleration.y -= .0015;
        if (acceleration.y < -.02) {
            acceleration.y = -.02f;
        }
        velocity.y += acceleration.y;
        prevDirection = Direction.UP;
    }

    private void downKey() {
        acceleration.y += .0015;
        if (acceleration.y > .02) {
            acceleration.y = .02f;
        }
        velocity.y += acceleration.y;
        prevDirection = Direction.DOWN;
    }

    public boolean isMining() {
        return mining;
    }

    private void addResistance() {
        acceleration.y -= .001f;
        if (acceleration.y < 0) {
            acceleration.y += 0.01f;
            if (acceleration.y > -.01f) {
                acceleration.y = 0;
            }
        } else if (acceleration.y > 0) {
            acceleration.y -= .01f;
            if (acceleration.y < .01f) {
                acceleration.y = 0;
            }
        }
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setDirection(int direction) {
        direction = 1;
    }

    @Override
    public java.awt.Rectangle getRectangle() {
        return this.boundingRectangle;
    }

    public boolean mine(AsteroidType type, Asteroid minedAsteroid) {
        if (input.isKeyDown(Input.KEY_SPACE)) {
            mining = true;
            miningLasers1 = new Vector2f(this.position.getX(), this.position.getY());
            mininglasers2 = new Vector2f(minedAsteroid.getX() + (minedAsteroid.maxResourceAmount /2), minedAsteroid.getY() + (minedAsteroid.maxResourceAmount / 2));
            int oldCommonResources = this.commonResources;
            if (type == AsteroidType.common) {
                this.commonResources += .1;
                 System.out.println("Current mined res: " + minedAsteroid.resourceAmount);
                 System.out.println("Current calc res: " + (commonResources - oldCommonResources));
            Communicator.sendData(Serializer.serializeAsteroidAsGamePacket(AsteroidComm.class.getSimpleName(), type, minedAsteroid.resourceAmount, (int)minedAsteroid.getX(), (int)minedAsteroid.getY()));
            minedAsteroid.resourceAmount -= .1;
            } else if (type == AsteroidType.magic) {
                this.magicResources++;
            } else if (type == AsteroidType.rare) {
                this.rareResources++;
            }
            return mining;
        }
       

        mining = false;
        return mining;
    }

    public void stopMining() {
        mining = false;
    }
}

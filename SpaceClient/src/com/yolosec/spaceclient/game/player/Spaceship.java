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
import com.yolosec.spaceclient.dao.interfaces.DrawableComponent;
import com.yolosec.spaceclient.game.world.Asteroid;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
import com.yolosec.spaceclient.game.world.GameWorldImpl;
import com.yolosec.spaceclient.game.world.Viewport;
import com.yolosec.spaceclient.gui.SpaceClient;
import static com.yolosec.spaceclient.gui.SpaceClient.screenHeight;
import static com.yolosec.spaceclient.gui.SpaceClient.screenWidth;
import java.awt.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Toon
 */
public class Spaceship extends GameObjectImpl {

    //id
    private int id = 3;

    //polygons
    private float[] polygonPoints;
    private Polygon polygon;

    //vectors
    private final Vector2f velocity = new Vector2f(0, 0);
    private final Vector2f acceleration = new Vector2f(0, 0);
    private Vector2f position, miningLasers1, mininglasers2, viewportPos;

    //numbers
    private final float horizontalAcceleration = 0, resistance = .001f, speed = 10;
    private final int width, height;
    private int commonResources = 0, magicResources = 0, rareResources = 0, direction;

    //misc
    private Direction prevDirection;
    private Rectangle boundingRectangle;
    private Input input;
    private boolean mining = false;

    /**
     * Gets the amount of resources for this spaceship.
     *
     * @return an int[] containing the amount of Common, Magic and Rare
     * resources - in that order.
     */
    public int[] getResources() {
        return new int[]{commonResources, magicResources, rareResources};
    }

    /**
     * Creates a new instance of type Spaceship. The Spacehip class is the
     * graphical representation of the player in the game. It can be controlled
     * with key input, and is a GameObject just like any other object in the
     * game. This constructor is used when creating a local (i.e. the "owning")
     * player.
     *
     * @param width the width of the spaceship.
     * @param height the height of the spaceship.
     */
    public Spaceship(int width, int height) {
        this.width = width;
        this.height = height;
        position = new Vector2f(100, 100);

        init();
    }

    /**
     * Creates a new instance of type Spaceship. The Spacehip class is the
     * graphical representation of the player in the game. It can be controlled
     * with key input, and is a GameObject just like any other object in the
     * game. This constructor is used when another player in the game needs to
     * be drawn and updated (i.e. multiplayer)
     *
     * @param fromPacket the packet from which the spaceship will be
     * constructed.
     */
    public Spaceship(SpaceshipComm fromPacket) {
        this.width = 10;
        this.height = 10;
        commonResources = fromPacket.getResources()[0];
        magicResources = fromPacket.getResources()[1];
        rareResources = fromPacket.getResources()[2];
        id = fromPacket.getId();
        fromPacket.getDirection();
        position = new Vector2f(fromPacket.getX(), fromPacket.getY());
        viewportPos = new Vector2f(screenWidth, screenHeight);
        init();
    }

    /**
     * Initializes the polygonpoints and the bounding rectangle for this
     * spaceship.
     */
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

    //@Override
    public void update(GameContainer gc) {
        this.input = gc.getInput();
        calculateMovement(input);
        boundingRectangle.x = (int) position.x;
        boundingRectangle.y = (int) position.y;
    }

    //@Override
    public void render(Graphics g, boolean self) {
//        if (self) {
//            polygon.setLocation(new Vector2f(SpaceClient.screenWidth / 2, SpaceClient.screenHeight / 2));
//        } else {
//            polygon.setLocation(position.x + (SpaceClient.screenWidth /2), position.y + (SpaceClient.screenHeight /2));
//        }
        if (self) {
            polygon.setLocation(position.x, position.y);
        } else {
            boolean toDraw1 = false;
            boolean toDraw2 = false;
            if (position.x >= Viewport.viewportPos.x && position.x <= Viewport.viewportPos.x + (SpaceClient.screenWidth - width)) {
                toDraw1 = true;
            }
            if (position.x >= Viewport.viewportPos.y && position.x <= Viewport.viewportPos.y + (SpaceClient.screenHeight - height)) {
                toDraw2 = true;
            }
            if (toDraw1 && toDraw2) {
                int drawPositionX = (int) (position.x - Viewport.viewportPos.x);
                int drawPositionY = (int) (position.y - Viewport.viewportPos.y);
                polygon.setLocation(drawPositionX, drawPositionY);
            }
        }

        g.drawRect((int) position.x, (int) position.y, width, height);
        if (mining) {
            g.drawLine(miningLasers1.x, miningLasers1.y, mininglasers2.x, mininglasers2.y);
        }

        if (self) {
            g.setColor(Color.blue);
        }
        g.draw(polygon);
        g.setColor(Color.white);
    }

    /**
     * Handles key input and transforms that input into movement on screen.
     *
     * @param input the input source to use.
     */
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

    /**
     * Sets the location for the spaceship.
     */
    private void setLocation() {
        //set x and y
        //position.add(velocity);

        Vector2f newTranslation = new Vector2f();
        //for every direction check if the spaceship is colliding with a border
        //if the player is colliding set its acceleration on the particular axis to 0
        //if the player is not colliding update his position

        if (this.position.x > 0) {
            newTranslation.x += this.velocity.x;
        } else {
            this.position.x = 1;
            this.velocity.x = 0;
            this.acceleration.x = 0;
        }

        if (this.position.x < (screenWidth - width)) {
            newTranslation.x += this.velocity.x;
        } else {
            this.position.x = screenWidth - width;
            this.velocity.x = 0;
            this.acceleration.x = 0;
        }

        if (this.position.y > 0) {
            newTranslation.y += this.velocity.y;
        } else {
            this.position.y = 1;
            this.velocity.y = 0;
            this.acceleration.y = 0;
        }

        if (this.position.y < (screenHeight - height)) {
            newTranslation.y += this.velocity.y;
        } else {
            this.position.y = (screenHeight - height);
            this.velocity.y = 0;
            this.acceleration.y = 0;
        }

        this.position.add(newTranslation);
        //this.position.add(velocity);

    }

    /**
     * Gets the ID associated with this spaceship which was obtained on login.
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Handles gamelogic when the left arrow key is pressed.
     */
    private void leftKey() {
        acceleration.y = 0;
        acceleration.x -= .0015;
        if (acceleration.x < -.01) {
            acceleration.x = -.01f;
        }
        velocity.x += acceleration.x;
    }

    /**
     * Handles gamelogic when the right arrow key is pressed.
     */
    private void rightKey() {
        acceleration.y = 0;
        acceleration.x += .0015;
        if (acceleration.x > .01) {
            acceleration.x = .01f;
        }

        velocity.x += acceleration.x;
    }

    /**
     *
     * @return the current Vector2f position of the spaceship.
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * Sets the spaceship position.
     *
     * @param position the new position.
     */
    public void setPosition(Vector2f position) {
        this.position = position;
    }

    /**
     * Handles gamelogic when the up arrow key is pressed.
     */
    private void upKey() {
        acceleration.y -= .0015;
        if (acceleration.y < -.02) {
            acceleration.y = -.02f;
        }
        velocity.y += acceleration.y;
        prevDirection = Direction.UP;
    }

    /**
     * Handles gamelogic when the down arrow key is pressed.
     */
    private void downKey() {
        acceleration.y += .0015;
        if (acceleration.y > .02) {
            acceleration.y = .02f;
        }
        velocity.y += acceleration.y;
        prevDirection = Direction.DOWN;
    }

    /**
     *
     * @return true if the spaceship is mining an asteroid, false otherwise.
     */
    public boolean isMining() {
        return mining;
    }

    /**
     * Adds resistance to the body of the spaceship, effectively applying a form
     * of physics.
     */
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

    /**
     *
     * @return the polygon for this spaceship.
     */
    public Polygon getPolygon() {
        return polygon;
    }

    /**
     * Sets the current direction for this spaceship.
     *
     * @param direction the new direction.
     */
    public void setDirection(int direction) {
        direction = 1;
    }

    //@Override
    public java.awt.Rectangle getRectangle() {
        return this.boundingRectangle;
    }

    /**
     * Called when the spaceship is currently colliding with an asteroid. If the
     * spacebar key is pressed, the player will start collection resources and
     * updates will be sent to the server.
     *
     * @param type the Type of Asteroid the player is mining.
     * @param minedAsteroid the Asteroid the player is mining.
     * @return true if the player is effectively mining (i.e. pressing
     * spacebar), false otherwise.
     */
    public boolean mine(AsteroidType type, Asteroid minedAsteroid) {
        if (input.isKeyDown(Input.KEY_SPACE)) {
            mining = true;
            int drawPositionX = (int) (minedAsteroid.getXPosition() - Viewport.viewportPos.x);
            int drawPositionY = (int) (minedAsteroid.getYPosition() - Viewport.viewportPos.y);
            miningLasers1 = new Vector2f(this.position.getX(), this.position.getY());
            mininglasers2 = new Vector2f(drawPositionX + (minedAsteroid.maxResourceAmount / 2), drawPositionY + (minedAsteroid.maxResourceAmount / 2));
            int oldCommonResources = this.commonResources;
            if (type == AsteroidType.common) {
                this.commonResources += 1;
                Communicator.sendData(Serializer.serializeAsteroidAsGamePacket(AsteroidComm.class.getSimpleName(), type, minedAsteroid.resourceAmount, (int) minedAsteroid.getXPosition(), (int) minedAsteroid.getYPosition()));
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

    /**
     * Sets the Common, Magic and Rare resources for this spacehip.
     *
     * @param resources the new int[] containing the new values for the
     * resources.
     */
    public void setResources(int[] resources) {
        this.commonResources = resources[0];
        this.magicResources = resources[1];
        this.rareResources = resources[2];
    }

    public Vector2f getAcceleration() {
        return this.acceleration;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}

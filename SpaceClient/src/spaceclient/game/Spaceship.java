/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import spaceclient.dao.interfaces.DrawableComponent;
import spaceclient.gui.Direction;
import spaceclient.gui.SpaceClient;

/**
 *
 * @author Toon
 */
public class Spaceship extends GameObject implements DrawableComponent {

    private int id = 2;

    private float[] polygonPoints;
    private Polygon polygon;
    private Vector2f position, relPosition;
    private float speed = 10;
    private Vector2f velocity;
    private Vector2f acceleration;
    private float horizontalAcceleration = 0;
    private Direction prevDirection;
    private float resistance = .001f;
    private int width, height;
    private Rectangle boundingRectangle;
    private int direction;

    public Spaceship(int width, int height, Rectangle boundingRectangle) {
        this.width = width;
        this.height = height;
        this.boundingRectangle = boundingRectangle;

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
        position = new Vector2f(0, 0);
        velocity = new Vector2f(0, 0);
        acceleration = new Vector2f(0, 0);
        prevDirection = Direction.NEUTRAL;
    }

    @Override
    public void update(GameContainer gc) {
        calculateMovement(gc.getInput());
    }

    @Override
    public void render(Graphics g) {
        polygon.setLocation(new Vector2f(SpaceClient.screenWidth / 2, SpaceClient.screenHeight / 2));
        g.drawString("X : " + position.x + " Y: " + position.y, 50, 50);
        g.drawString("Accel: " + acceleration, 100, 100);
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

        addResistance();
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
        if (acceleration.y < -.2) {
            acceleration.y = -.2f;
        }
        velocity.y += acceleration.y;
        if (velocity.y > .08) {
            velocity.y = .08f;
        }
        prevDirection = Direction.UP;
    }

    private void downKey() {
        acceleration.y += .0015;
        if (acceleration.y > .2) {
            acceleration.y = .2f;
        }
        velocity.y += acceleration.y;
        if (velocity.y > .02) {
            velocity.y = .08f;
        }
        prevDirection = Direction.DOWN;
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

}

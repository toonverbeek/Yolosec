package spaceclient;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class SpaceClient extends BasicGame {

    private Vector2f position;
    private float speed = 10;
    private Vector2f velocity;
    private Vector2f acceleration;
    private float horizontalAcceleration = 0;
    private Direction prevDirection;
    private float resistance = .001f;
    //rotation in degrees
    private int rotationInDegrees;

    //spaceship
    private Polygon spaceship;

    public SpaceClient(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        float[] polygonPoints = new float[8];
        polygonPoints[0] = 0;
        polygonPoints[1] = 0;
        polygonPoints[2] = 10;
        polygonPoints[3] = 0;
        polygonPoints[4] = 0;
        polygonPoints[5] = 10;
        polygonPoints[6] = 10;
        polygonPoints[7] = 10;
        spaceship = new Polygon(polygonPoints);
        position = new Vector2f(0, 0);
        velocity = new Vector2f(0, 0);
        acceleration = new Vector2f(0,0);
        prevDirection = Direction.NEUTRAL;
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        checkKeys(gc.getInput());
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        spaceship.setLocation(position);
        g.drawString("X : " + position.x + " Y: " + position.y, 50, 50);
        g.drawString("Accel: " + acceleration, 100, 100);
        g.draw(spaceship);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new SpaceClient("Simple Slick Game"));
            appgc.setDisplayMode(1366, 768, true);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(SpaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void checkKeys(Input input) {
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

    private void leftKey() {
        acceleration.y = 0;
        acceleration.x -= .0015;
        if(acceleration.x < -.01) {
            acceleration.x = -.01f;
        }
        velocity.x += acceleration.x;
    }

    private void rightKey() {
        acceleration.y = 0;
        acceleration.x += .0015;
        if(acceleration.x > .01) {
            acceleration.x = .01f;
        }
        
        velocity.x += acceleration.x;
    }

    private void upKey() {
        acceleration.y -= .015;
        if (acceleration.y < -2) {
            acceleration.y = -2f;
        }
        velocity.y += acceleration.y;
        if(velocity.y > .8) {
            velocity.y = .8f;
        }
        prevDirection = Direction.UP;
    }

    private void downKey() {
        acceleration.y += .015;
        if (acceleration.y > 2) {
            acceleration.y = 2f;
        }
        velocity.y += acceleration.y;
        if(velocity.y > .2) {
            velocity.y = .8f;
        }
        prevDirection = Direction.DOWN;
    }

    /*
     Adds resistance to the acceleration, slowly putting it to a halt. 
     */
    private void addResistance() {
        acceleration.y -= .001f;
        if (acceleration.y < 0) {
            acceleration.y += 0.01f;
            if(acceleration.y > -.01f) {
                acceleration.y = 0;
            }
        } else if(acceleration.y > 0) {
            acceleration.y -= .01f;
            if(acceleration.y < .01f) {
                acceleration.y = 0;
            }
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            System.exit(0);
        }
    }
}

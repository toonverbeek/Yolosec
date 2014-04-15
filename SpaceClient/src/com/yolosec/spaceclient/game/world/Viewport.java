/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game.world;

import com.ptsesd.groepb.shared.AsteroidType;
import com.yolosec.spaceclient.dao.interfaces.DrawableComponent;
import com.yolosec.spaceclient.dao.interfaces.GameObject;
import com.yolosec.spaceclient.game.player.Direction;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.gui.SpaceClient;
import static com.yolosec.spaceclient.gui.SpaceClient.screenHeight;
import static com.yolosec.spaceclient.gui.SpaceClient.screenWidth;
import java.awt.Rectangle;
import java.util.List;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author Lisanne
 */
public class Viewport extends GameObjectImpl implements DrawableComponent {

    private final Spaceship spaceship;
    public static Vector2f viewportPos;

    //vectors
    private final Vector2f viewportVelocity = new Vector2f(0, 0);
    private final Vector2f viewportAccelaration = new Vector2f(0, 0);

    // private final Vector2f viewportVelocity = new Vector2f(0, 0);
    // private final Vector2f viewportAcceleration = new Vector2f(0, 0);
    //misc
    private Direction prevDirection;
    private Rectangle boundingRectangle;

    private Vector2f position;
    private Input input;

    public static long VIEWPORTSCROLL = 100;
    private final TiledMap tileMap;
    private float tilemapWidth, tilemapHeight;
    private static final int TILESIZE = 32;

    public Viewport(Spaceship player, TiledMap tileMap) {
        this.spaceship = player;
        viewportPos = new Vector2f(screenWidth, screenHeight);
        position = new Vector2f();
        this.tileMap = tileMap;
        this.tilemapHeight = tileMap.getHeight();
        this.tilemapWidth = tileMap.getWidth();
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

    @Override
    public Rectangle getRectangle() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return spaceship.getRectangle();
    }

    @Override
    public void update(GameContainer gc) {
        spaceship.update(gc);

        this.input = gc.getInput();
        this.calculateMovement(input);
    }
    
    public void renderGameObjects(Graphics g, AngelCodeFont resourceFont, List<GameObjectImpl> gameObjects){
        for (GameObject gObject : gameObjects) {
            if (gObject instanceof Asteroid) {
                Asteroid ast = (Asteroid) gObject;
                ast.setResourceFont(resourceFont);
                ast.render(g, false);
            } else if (gObject instanceof Spaceship) {
                Spaceship spaceship2 = (Spaceship) gObject;
                spaceship2.render(g, false);
            }
        }
    }
    @Override
    public void render(Graphics g, boolean self) {
        int x = (int) this.viewportPos.getX();
        int y = (int) this.viewportPos.getY();
        try {
            tileMap.render(0, 0, x / TILESIZE, y / TILESIZE, (screenWidth / TILESIZE) + 2, (screenHeight / TILESIZE) + 2);
        } catch (ArithmeticException ex) {
            ex.printStackTrace();
        }
        spaceship.render(g, self);
    }

    /**
     * Handles key input and transforms that input into movement on screen.
     *
     * @param input the input source to use.
     */
    private void calculateMovement(Input input) {
        //update the position according to the velocity
        updateViewportLocation();
    }

    private void updateViewportLocation() {

        //moving left
        if (spaceship.getPosition().x <= VIEWPORTSCROLL) {
            viewportVelocity.x = -2f;
        } else if (spaceship.getPosition().x >= ((screenWidth - spaceship.getWidth()) - VIEWPORTSCROLL)) {
            viewportVelocity.x = 2f;
        } else {
            viewportVelocity.x = 0;
        }
        if (spaceship.getPosition().y <= VIEWPORTSCROLL) {
            viewportVelocity.y = -2f;
        } else if (spaceship.getPosition().y >= ((screenHeight - spaceship.getHeight()) - VIEWPORTSCROLL)) {
            viewportVelocity.y = 2f;
        } else {
            viewportVelocity.y = 0;
        }

        //calculate if changes in speed
        //viewportVelocity.add(viewportAccelaration);
        //apply speed to position
        float newPosX = viewportPos.x + viewportVelocity.x;
        if (newPosX >= 0 && newPosX <= (tilemapWidth * TILESIZE) - spaceship.getWidth()) {
            viewportPos.x += viewportVelocity.x;
        }

        float newPosY = viewportPos.y + viewportVelocity.y;
        if (newPosY >= 0 && newPosY <= (tilemapHeight * TILESIZE) - spaceship.getHeight()) {
            viewportPos.y += viewportVelocity.y;
        }
    }
}

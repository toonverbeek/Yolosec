/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game.world;

import com.yolosec.spaceclient.dao.interfaces.DrawableComponent;
import com.yolosec.spaceclient.game.player.Direction;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.gui.SpaceClient;
import static com.yolosec.spaceclient.gui.SpaceClient.screenHeight;
import static com.yolosec.spaceclient.gui.SpaceClient.screenWidth;
import java.awt.Rectangle;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author Lisanne
 */
public class Viewport extends GameObjectImpl implements DrawableComponent {

    public static Spaceship spaceship;
    public static Vector2f viewportPos;

    //vectors
    private final Vector2f viewportVelocity = new Vector2f(0, 0);
    private final Vector2f viewportAcceleration = new Vector2f(0, 0);

    public static long VIEWPORTSCROLL = 100;
    private final TiledMap tileMap;
    public static float tilemapWidth, tilemapHeight;
    public static final int TILESIZE = 32;

    public Viewport(Spaceship player, TiledMap tileMap) {
        Viewport.spaceship = player;
        System.out.println("Viewport player1: " + player.getId());
        System.out.println("Viewport player2: " + Viewport.spaceship.getId());
        this.tileMap = tileMap;
        Viewport.tilemapHeight = tileMap.getHeight() * TILESIZE;
        Viewport.tilemapWidth = tileMap.getWidth() * TILESIZE;
        Viewport.viewportPos = new Vector2f(player.getPosition().x - (screenWidth/2), player.getPosition().y - (screenHeight/2));
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

    @Override
    public Rectangle getRectangle() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new Rectangle((int) Viewport.viewportPos.x, (int) Viewport.viewportPos.y, screenWidth, screenHeight);
    }

    @Override
    public void update(GameContainer gc) {
        updateViewportLocation(gc);
        spaceship.update(gc);
    }

    @Override
    public void render(Graphics g, boolean self) {
        int x = (int) Viewport.viewportPos.getX();
        int y = (int) Viewport.viewportPos.getY();
        try {
            tileMap.render(0, 0, x / TILESIZE, y / TILESIZE, (screenWidth / TILESIZE) + 2, (screenHeight / TILESIZE) + 2);
        } catch (ArithmeticException ex) {
            ex.printStackTrace();
        }
        spaceship.render(g, self);
    }

    private void updateViewportLocation(GameContainer gc) {
        calculateMovement(gc.getInput());
        Vector2f newPos = new Vector2f(viewportPos);
        newPos.add(viewportVelocity);

        Vector2f newPlayerPosition = new Vector2f(spaceship.getPosition());
        float playerX = newPlayerPosition.x;
        float playerY = newPlayerPosition.y;

        //update the position of the player
        //update the position only when the position is NOT in the top left corner
        if (newPos.x > - (screenWidth/2) && newPos.x <= (tilemapWidth - (screenWidth/2))) {
            viewportPos.x += viewportVelocity.x;
            playerX = viewportPos.x + (screenWidth / 2) - (spaceship.getWidth() / 2);
            spaceship.setPosition(new Vector2f(playerX, playerY));
        } else {
            if (newPos.x < screenWidth) {
                viewportPos.x = -(screenWidth/2);
                viewportVelocity.x = 0;
            } else {
                viewportPos.x = (tilemapWidth - (screenWidth/2));
                viewportVelocity.x = 0;
            }
        }
        if (newPos.y > - (screenHeight/2) && newPos.y < (tilemapHeight - (screenHeight/2))) {
            viewportPos.y += viewportVelocity.y;
            playerY = viewportPos.y + (screenHeight / 2) - (spaceship.getHeight() / 2);
            spaceship.setPosition(new Vector2f(playerX, playerY));
        } else {
            if (newPos.y < screenHeight) {
                viewportPos.y = -(screenHeight/2);
                viewportVelocity.y = 0;
            } else {
                viewportPos.y = (tilemapHeight - (screenHeight/2));
                viewportVelocity.y = 0;
            }
        }
    }

    /**
     * Handles gamelogic when the left arrow key is pressed.
     */
    private void leftKey() {
        viewportAcceleration.y = 0;
        viewportAcceleration.x -= .0015;
        if (viewportAcceleration.x < -.01) {
            viewportAcceleration.x = -.01f;
        }
        viewportVelocity.x += viewportAcceleration.x;
    }

    /**
     * Handles gamelogic when the right arrow key is pressed.
     */
    private void rightKey() {
        viewportAcceleration.y = 0;
        viewportAcceleration.x += .0015;
        if (viewportAcceleration.x > .01) {
            viewportAcceleration.x = .01f;
        }

        viewportVelocity.x += viewportAcceleration.x;
    }

    /**
     * Handles gamelogic when the up arrow key is pressed.
     */
    private void upKey() {
        viewportAcceleration.y -= .0015;
        if (viewportAcceleration.y < -.02) {
            viewportAcceleration.y = -.02f;
        }
        viewportVelocity.y += viewportAcceleration.y;
    }

    /**
     * Handles gamelogic when the down arrow key is pressed.
     */
    private void downKey() {
        viewportAcceleration.y += .0015;
        if (viewportAcceleration.y > .02) {
            viewportAcceleration.y = .02f;
        }
        viewportVelocity.y += viewportAcceleration.y;
    }

    /**
     * Handles key input and transforms that input into movement on screen.
     *
     * @param input the input source to use.
     */
    private void calculateMovement(Input input) {
        if (input.isKeyDown(Input.KEY_LEFT)) {
            if (!spaceship.isAllowedToUpdateX()) {
                leftKey();
            }
        } else if (input.isKeyDown(Input.KEY_UP)) {
            upKey();
        } else if (input.isKeyDown(Input.KEY_RIGHT)) {
            if (!spaceship.isAllowedToUpdateX()) {
                rightKey();
            }
        } else if (input.isKeyDown(Input.KEY_DOWN)) {
            downKey();
        }
    }
}

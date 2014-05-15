/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game.world;

import com.yolosec.spaceclient.dao.interfaces.DrawableComponent;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.gui.SpaceClient;
import static com.yolosec.spaceclient.gui.SpaceClient.screenHeight;
import static com.yolosec.spaceclient.gui.SpaceClient.screenWidth;
import java.awt.Rectangle;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
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

    public static long VIEWPORTSCROLL = 100;
    private final TiledMap tileMap;
    public static float tilemapWidth, tilemapHeight;
    public static final int TILESIZE = 32;

    public Viewport(Spaceship player, TiledMap tileMap) {
        this.spaceship = player;
        this.tileMap = tileMap;
        Viewport.tilemapHeight = tileMap.getHeight();
        Viewport.tilemapWidth = tileMap.getWidth();

        Viewport.viewportPos = new Vector2f();
        float drawPositionX = player.getPosition().x;
        float drawPositionY = player.getPosition().y;
        if (drawPositionX > (tilemapWidth * TILESIZE) - screenWidth) {
            Viewport.viewportPos.x = (tilemapWidth * TILESIZE) - screenWidth;
        } else if (drawPositionX < screenWidth) {
            Viewport.viewportPos.x = drawPositionX;
        } else {
            Viewport.viewportPos.x = drawPositionX;
        }
        if (drawPositionY > (tilemapWidth * TILESIZE) - screenHeight) {
            Viewport.viewportPos.y = (tilemapHeight * TILESIZE) - screenHeight;
        } else if (drawPositionY < screenHeight) {
            Viewport.viewportPos.y = drawPositionY;
        } else {
            Viewport.viewportPos.y = drawPositionY;
        }

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
        updateViewportLocation();
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

    private void updateViewportLocation() {
        Vector2f spaceshipPosition = new Vector2f(spaceship.getPosition());
        Vector2f viewportPosition = new Vector2f(viewportPos);
        Vector2f spaceShipDrawPosition = spaceshipPosition.sub(viewportPosition);

        boolean viewport_X_0 = false;
        boolean viewport_X_1 = false;
        boolean viewport_Y_0 = false;
        boolean viewport_Y_1 = false;

        if (viewportPos.x > 0) {
            viewport_X_0 = true;
        } else {
            viewport_X_0 = false;
        }
        if (viewportPos.y > 0) {
            viewport_Y_0 = true;
        } else {
            viewport_Y_0 = false;
        }

        if (viewportPos.x < (TILESIZE * tilemapWidth) - (3 * TILESIZE)) {
            viewport_X_1 = true;
        } else {
            viewport_X_1 = false;
        }

        if (viewportPos.y < (TILESIZE * tilemapHeight) - (3 * TILESIZE)) {
            viewport_Y_1 = true;
        } else {
            viewport_Y_1 = false;
        }

        if (viewport_X_0 && viewport_X_1) {
            if (spaceShipDrawPosition.x > (SpaceClient.screenWidth - VIEWPORTSCROLL + (3 * TILESIZE)) - spaceship.getWidth()) {
                this.viewportVelocity.x = 2f;
            } else if (spaceShipDrawPosition.x < VIEWPORTSCROLL) {
                this.viewportVelocity.x = -2f;
            } else {
                this.viewportVelocity.x = 0;
            }
            viewportPos.x += viewportVelocity.x;
        }
        if (viewport_Y_0 && viewport_Y_1) {

            if (spaceShipDrawPosition.y > (SpaceClient.screenHeight - VIEWPORTSCROLL + (3 * TILESIZE)) - spaceship.getHeight()) {
                this.viewportVelocity.y = 2f;
            } else if (spaceShipDrawPosition.y < VIEWPORTSCROLL) {
                this.viewportVelocity.y = -2f;
            } else {
                this.viewportVelocity.y = 0;
            }
            viewportPos.y += viewportVelocity.y;
        }
    }
}

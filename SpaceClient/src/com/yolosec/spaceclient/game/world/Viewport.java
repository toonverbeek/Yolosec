/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game.world;

import com.yolosec.spaceclient.dao.interfaces.DrawableComponent;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.gui.Minimap;
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
        int drawPositionX = (int) (player.getPosition().x + (screenWidth / 2));
        int drawPositionY = (int) (player.getPosition().y + (screenHeight / 2));
        Viewport.viewportPos = new Vector2f(drawPositionX, drawPositionY);
        this.tileMap = tileMap;
        Viewport.tilemapHeight = tileMap.getHeight();
        Viewport.tilemapWidth = tileMap.getWidth();
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

    @Override
    public Rectangle getRectangle() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new Rectangle((int)Viewport.viewportPos.x, (int)Viewport.viewportPos.y, screenWidth, screenHeight);
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

        int drawSpaceshipPositionX = (int) (this.spaceship.getPosition().x - Viewport.viewportPos.x);
        int drawSpaceshipPositionY = (int) (this.spaceship.getPosition().y - Viewport.viewportPos.y);
        //moving left
        if (drawSpaceshipPositionX <= VIEWPORTSCROLL) {
            viewportVelocity.x = -5f;
        } else if (drawSpaceshipPositionX >= ((screenWidth - spaceship.getWidth()) - VIEWPORTSCROLL)) {
            viewportVelocity.x = 5f;
        } else {
            viewportVelocity.x = 0;
        }
        if (drawSpaceshipPositionY <= VIEWPORTSCROLL) {
            viewportVelocity.y = -5f;
        } else if (drawSpaceshipPositionY >= ((screenHeight - spaceship.getHeight()) - VIEWPORTSCROLL)) {
            viewportVelocity.y = 5f;
        } else {
            viewportVelocity.y = 0;
        }
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

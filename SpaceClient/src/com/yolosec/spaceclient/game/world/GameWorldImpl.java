/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game.world;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import com.yolosec.spaceclient.dao.GameObjectDAOImpl;
import com.yolosec.spaceclient.dao.interfaces.DrawableComponent;
import com.yolosec.spaceclient.dao.interfaces.GameObject;
import com.yolosec.spaceclient.dao.interfaces.GameWorld;
import com.yolosec.spaceclient.gui.Camera;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.observing.NodeImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.newdawn.slick.AngelCodeFont;

/**
 *
 * @author Toon
 */
public class GameWorldImpl extends NodeImpl<GameObject> implements DrawableComponent, GameWorld {

    private TiledMap tileMap;
    private GameObjectDAOImpl gameObjectDAO;
    private Spaceship player;
    private Camera camera;
    private List<GameObjectImpl> gameObjects = new ArrayList<>();
    private HashMap<String, AngelCodeFont> fontSet;

    public GameWorldImpl(Spaceship player) throws SlickException {
        System.out.println("Constructin gameworldimpl");
        gameObjectDAO = new GameObjectDAOImpl();
        tileMap = new TiledMap("/map.tmx");
        this.player = player;
        camera = new Camera(tileMap, tileMap.getWidth(), tileMap.getHeight());
    }

    @Override
    public void update(GameContainer gc) {
        player.update(gc);
        gameObjectDAO.sendData(player);
        gameObjects.clear();
        gameObjects.addAll(gameObjectDAO.getGameObjects());
        for (GameObjectImpl gObject : gameObjects) {
            if (gObject instanceof Asteroid) {
                Asteroid ast = (Asteroid) gObject;
                ast.updateAsteroid(gc, player);
                if (ast.isIntersecting(player)) {
                    //if asteroid is being mined, start sending updates to server 
                    gameObjectDAO.sendData(ast);
                }
            } else if (gObject instanceof Spaceship) {
                Spaceship spaceship = (Spaceship) gObject;
                System.out.println(spaceship.getPosition().x +  "  "  + spaceship.getPosition().y);
                spaceship.update(gc);
            }
        }
    }

    @Override
    public void render(Graphics g, boolean self) {
//        int tileCountWidth = screenWidth / tileWidth;
//        int tileCountHeight = screenHeight / tileHeight;
//        tileMap.render((int) player.getSpaceship().getPosition().x + (tileWidth * 2), (int) player.getSpaceship().getPosition().y + (tileHeight * 2), mapX, mapY, mapX + tileCountWidth, mapY + tileCountHeight);
//        camera.translate(g, player.getSpaceship());

        g.drawString("Amount of gameObjects " +gameObjects.size(), 50, 250);
        player.render(g, true);
        AngelCodeFont resourceFont = fontSet.get("resource_font");
        for (GameObject gObject : gameObjects) {
            if (gObject instanceof Asteroid) {
                Asteroid ast = (Asteroid) gObject;
                ast.setResourceFont(resourceFont);
                ast.render(g, false);
            } else if (gObject instanceof Spaceship) {
                Spaceship spaceship = (Spaceship) gObject;
                spaceship.render(g, false);
            }
        }
    }

    public void setFontSet(HashMap<String, AngelCodeFont> fontSet) {
        this.fontSet = fontSet;
    }

}

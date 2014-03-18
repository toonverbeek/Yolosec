/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import com.yolosec.spaceclient.dao.GameObjectDAOImpl;
import com.yolosec.spaceclient.dao.interfaces.DrawableComponent;
import com.yolosec.spaceclient.dao.interfaces.GameObject;
import com.yolosec.spaceclient.dao.interfaces.GameWorld;
import com.yolosec.spaceclient.observing.NodeImpl;

/**
 *
 * @author Toon
 */
public class GameWorldImpl extends NodeImpl<GameObject> implements DrawableComponent, GameWorld {

    private TiledMap tileMap;
    private GameObjectDAOImpl gameObjectDAO;
    private Spaceship player;
    private Camera camera;

    public GameWorldImpl(Spaceship player) throws SlickException {
        gameObjectDAO = new GameObjectDAOImpl();
        tileMap = new TiledMap("/map.tmx");
        this.player = player;
        camera = new Camera(tileMap, tileMap.getWidth(), tileMap.getHeight());
    }

    @Override
    public void update(GameContainer gc) {
        player.update(gc);
        gameObjectDAO.sendData(player);
        for (GameObjectImpl gObject : gameObjectDAO.getGameObjects()) {
            if (gObject instanceof Asteroid) {
                Asteroid ast = (Asteroid) gObject;
                ast.updateAsteroid(gc, player);
                if (ast.isIntersecting(player)) {
                    gameObjectDAO.sendData(ast);
                }
            } else if (gObject instanceof Spaceship) {
                Spaceship spaceship = (Spaceship) gObject;
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

        player.render(g, true);
        for (GameObjectImpl gObject : gameObjectDAO.getGameObjects()) {
            if (gObject instanceof Asteroid) {
                Asteroid ast = (Asteroid) gObject;
                ast.render(g, false);
            } else if (gObject instanceof Spaceship) {
                Spaceship spaceship = (Spaceship) gObject;
                spaceship.render(g, false);
            }
        }
    }

}

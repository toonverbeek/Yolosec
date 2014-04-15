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
import java.util.List;
import org.newdawn.slick.AngelCodeFont;

/**
 *
 * @author Toon
 */
public class GameWorldImpl extends NodeImpl<GameObject> implements DrawableComponent, GameWorld {
    /**
     * The TiledMap that will define the location of the GameObjects and Players.
     */
    private TiledMap tileMap;
    public static long MAPSIZE = 8000;
    
    /**
     * The GameObjectDAO is the link between the GameServer and the Gameworld.
     * It passes all the new incoming GameObjects to the GameWorld from the Gameserver.
     */
    private GameObjectDAOImpl gameObjectDAO;
    
    private Viewport playerViewport;
    
    //private Camera camera;
    
    
    /**
     * A list of all active GameObjects.
     * The GameObjects in this list will be drawn and updated as necessary. 
     */
    private List<GameObjectImpl> gameObjects = new ArrayList<>();
    
    private HashMap<String, AngelCodeFont> fontSet;

    /**
     * Creates a new instance of GameWorldImpl.
     * The GameWorld handles the update and draw methods for all GameObjects.
     * The GameWorld also handles the drawing of the Map.
     * @param player the player owning this instance.
     * @throws SlickException 
     */
    public GameWorldImpl(Spaceship player) throws SlickException {
        System.out.println("Constructin gameworldimpl");
        this.gameObjectDAO = new GameObjectDAOImpl();
        tileMap = new TiledMap("/newmap.tmx");
        
        this.playerViewport = new Viewport(player, tileMap);
        //camera = new Camera(tileMap, tileMap.getWidth(), tileMap.getHeight());
    }

    @Override
    public void update(GameContainer gc) {
        playerViewport.update(gc);
        gameObjectDAO.sendData(playerViewport);
        gameObjects.clear();
        gameObjects.addAll(gameObjectDAO.getGameObjects());
        for (GameObjectImpl gObject : gameObjects) {
            if (gObject instanceof Asteroid) {
                Asteroid ast = (Asteroid) gObject;
                ast.updateAsteroid(gc, playerViewport.getSpaceship());
                if (ast.isIntersecting(playerViewport)) {
                    //if asteroid is being mined, start sending updates to server 
                    //gameObjectDAO.sendData(ast);
                }
            } else if (gObject instanceof Spaceship) {
                Spaceship spaceship = (Spaceship) gObject;
                //playerViewport.getSpaceship().update(gc);
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

        
        //player.render(g, true);
        playerViewport.render(g, true);
        
        g.drawString("X : " + playerViewport.getSpaceship().getPosition().x + " Y: " + playerViewport.getSpaceship().getPosition().y, 50, 50);
        g.drawString("Accel: " + playerViewport.getSpaceship().getAcceleration(), 50, 70);
        g.drawString("Common Resources: " + playerViewport.getSpaceship().getResources()[0], 50, 90);
        g.drawString("Magic Resources: " + playerViewport.getSpaceship().getResources()[1], 50, 110);
        g.drawString("Rare Resources: " + playerViewport.getSpaceship().getResources()[2], 50, 130);
        g.drawString("Amount of gameObjects " + gameObjects.size(), 50, 250);
        
        AngelCodeFont resourceFont = fontSet.get("resource_font");
        playerViewport.renderGameObjects(g, resourceFont,gameObjects);
        
    }

    public void setFontSet(HashMap<String, AngelCodeFont> fontSet) {
        this.fontSet = fontSet;
    }
}

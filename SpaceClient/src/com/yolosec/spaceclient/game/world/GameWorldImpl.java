/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game.world;

import com.ptsesd.groepb.shared.PlanetComm;
import com.ptsesd.groepb.shared.PlanetsComm;
import com.yolosec.spaceclient.dao.GameObjectDAOImpl;
import com.yolosec.spaceclient.dao.interfaces.DrawableComponent;
import com.yolosec.spaceclient.dao.interfaces.GameObject;
import com.yolosec.spaceclient.dao.interfaces.GameWorld;
import com.yolosec.spaceclient.game.player.Inventory;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.gui.Camera;
import com.yolosec.spaceclient.gui.GameState;
import com.yolosec.spaceclient.gui.Minimap;
import com.yolosec.spaceclient.gui.SpaceClient;
import static com.yolosec.spaceclient.gui.SpaceClient.screenWidth;
import com.yolosec.spaceclient.observing.NodeImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author Toon
 */
public class GameWorldImpl extends NodeImpl<GameObject> implements DrawableComponent, GameWorld {

    /**
     * The TiledMap that will define the location of the GameObjects and
     * Players.
     */
    private TiledMap tileMap;
    public static long MAPSIZE = 8000;

    /**
     * The GameObjectDAO is the link between the GameServer and the Gameworld.
     * It passes all the new incoming GameObjects to the GameWorld from the
     * Gameserver.
     */
    private GameObjectDAOImpl gameObjectDAO;

    private Viewport playerViewport;

    /**
     * A list of all active GameObjects. The GameObjects in this list will be
     * drawn and updated as necessary.
     */
    private List<GameObjectImpl> gameObjects = new ArrayList<>();
    private List<Planet> planets = new ArrayList<>();
    private HashMap<String, AngelCodeFont> fontSet;
    private final Minimap minimap;
    private Inventory playerInventory;
    private GameState state;

    /**
     * Creates a new instance of GameWorldImpl. The GameWorld handles the update
     * and draw methods for all GameObjects. The GameWorld also handles the
     * drawing of the Map.
     *
     * @param player the player owning this instance.
     * @throws SlickException
     */
    public GameWorldImpl(GameState state, Spaceship player) throws SlickException {
        this.state = state;
        System.out.println("Constructin gameworldimpl");
        this.gameObjectDAO = new GameObjectDAOImpl();
        tileMap = new TiledMap("/map_space.tmx");

        this.playerViewport = new Viewport(player, tileMap);
        minimap = new Minimap(screenWidth - 200, 0, 200, 200);
        generatePlanets();
        for (Planet p : planets) {
            gameObjects.add(p);
        }
    }

    @Override
    public void update(GameContainer gc) {
        playerViewport.update(gc);
        gameObjectDAO.sendData(playerViewport.getSpaceship());
        gameObjects.clear();
        gameObjects.addAll(gameObjectDAO.getGameObjects());
        for (Planet p : planets) {
            gameObjects.add(p);
        }
        for (GameObjectImpl gObject : gameObjects) {
            if (gObject instanceof Asteroid) {
                Asteroid ast = (Asteroid) gObject;
                ast.updateAsteroid(gc, playerViewport.getSpaceship());
                if (ast.isIntersecting(playerViewport.getSpaceship())) {
                    //if asteroid is being mined, start sending updates to server 
                    //gameObjectDAO.sendData(ast);
                }
            } else if (gObject instanceof Spaceship) {
                Spaceship spaceship = (Spaceship) gObject;
                spaceship.update(gc);
                //check planet collision && input key
            } else if (gObject instanceof Inventory) {
                Inventory inventory = (Inventory) gObject;
                if (inventory.getSpaceshipId() > 0) {
                    SpaceClient.playerInventory = inventory;
                } else {
                    SpaceClient.auctionhouseInventory = inventory;
                }
            } else if (gObject instanceof Planet) {
                Planet planet = (Planet) gObject;
                System.out.println("Planet Name: " + planet.getName());
                if (gc.getInput().isKeyPressed(Input.KEY_A)) {
                    System.out.println("Planet Rectangle; X: " + planet.getRectangle().x + "|Y: " + planet.getRectangle().y + "| Size: " + planet.getRectangle().getHeight());
                    System.out.println("Spaceship Rectangle; X: " + playerViewport.getSpaceship().getGlobalRectangle().x + "|Y: " + playerViewport.getSpaceship().getGlobalRectangle().y + "| Size: " + playerViewport.getSpaceship().getGlobalRectangle().getHeight());
                    if (planet.getRectangle().intersects(playerViewport.getSpaceship().getGlobalRectangle())) {
                        state.toPlanetState();
                    }
                }
            }
        }

    }

    @Override
    public void render(Graphics g, boolean self) {
//        int tileCountWidth = screenWidth / tileWidth;
//        int tileCountHeight = screenHeight / tileHeight;
//        tileMap.render((int) player.getSpaceship().getPosition().x + (tileWidth * 2), (int) player.getSpaceship().getPosition().y + (tileHeight * 2), mapX, mapY, mapX + tileCountWidth, mapY + tileCountHeight);
//        camera.translate(g, player.getSpaceship());

        playerViewport.render(g, true);

        g.drawString("X : " + playerViewport.getSpaceship().getPosition().x + " Y: " + playerViewport.getSpaceship().getPosition().y, 50, 50);
        g.drawString("Accel: " + playerViewport.getSpaceship().getAcceleration(), 50, 70);
        g.drawString("Common Resources: " + playerViewport.getSpaceship().getResources()[0], 50, 90);
        g.drawString("Magic Resources: " + playerViewport.getSpaceship().getResources()[1], 50, 110);
        g.drawString("Rare Resources: " + playerViewport.getSpaceship().getResources()[2], 50, 130);
        g.drawString("Amount of gameObjects " + gameObjects.size(), 50, 250);

        AngelCodeFont resourceFont = fontSet.get("resource_font");
        for (GameObject gObject : gameObjects) {
            if (gObject instanceof Asteroid) {
                Asteroid ast = (Asteroid) gObject;
                ast.setResourceFont(resourceFont);
                ast.render(g, false);
            } else if (gObject instanceof Spaceship) {
                Spaceship spaceship2 = (Spaceship) gObject;
                spaceship2.render(g, false);
            } else if (gObject instanceof Planet) {
                Planet planet = (Planet) gObject;
                planet.render(g, false);
            }
        }
        minimap.render(g, playerViewport.getSpaceship(), gameObjects);
    }

    public void setFontSet(HashMap<String, AngelCodeFont> fontSet) {
        this.fontSet = fontSet;
    }

    private void generatePlanets() {
        //generate the planets, from static PlanetsComm
        List<PlanetComm> planetComms = new PlanetsComm().getPlanets();
        for (PlanetComm planet : planetComms) {
            Planet actualPlanet = new Planet(planet.getSize(), planet.getX(), planet.getY(), planet.getPlanetName());
            planets.add(actualPlanet);
        }
    }
}
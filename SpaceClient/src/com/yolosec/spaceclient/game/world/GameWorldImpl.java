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
import com.yolosec.spaceclient.game.player.User;
import com.yolosec.spaceclient.gui.Camera;
import com.yolosec.spaceclient.gui.Chat;
import com.yolosec.spaceclient.gui.GameState;
import com.yolosec.spaceclient.gui.Minimap;
import com.yolosec.spaceclient.gui.SpaceClient;
import static com.yolosec.spaceclient.gui.SpaceClient.screenHeight;
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
    private List<Spaceship> spaceships = new ArrayList<>();
    private HashMap<String, AngelCodeFont> fontSet;
    private final Minimap minimap;
    private Chat chat;
    private Inventory playerInventory;
    private SpaceClient client;
    private Thread chatThread;

    /**
     * Creates a new instance of GameWorldImpl. The GameWorld handles the update
     * and draw methods for all GameObjects. The GameWorld also handles the
     * drawing of the Map.
     *
     * @param state
     * @param player the player owning this instance.
     * @throws SlickException
     */
    public GameWorldImpl(SpaceClient client, User player) throws SlickException {
        this.client = client;
        tileMap = new TiledMap("/map_space.tmx");
        this.playerViewport = new Viewport(player.getSpaceship(), tileMap);
        System.out.println("Constructin gameworldimpl");
        this.gameObjectDAO = new GameObjectDAOImpl();

        minimap = new Minimap(screenWidth - 200 - 1, 1, 200 - 1, 200);
        chat = new Chat(1 + (int) (screenWidth * 0.25), (int) (screenHeight * 0.75), (int) (screenWidth * 0.50) - 1, (int) (screenHeight * 0.25), player);
        chatThread = new Thread(chat);
        chatThread.start();
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
        spaceships.clear();
        gameObjects.addAll(gameObjectDAO.getGameObjects());
        //spaceships.addAll(gameObjectDAO.getSpaceships());
        for (Planet p : planets) {
            gameObjects.add(p);
        }
//        for(Spaceship s : spaceships){
//            s.update(gc);
//        }
        for (GameObjectImpl gObject : gameObjects) {
            if (gObject instanceof Asteroid) {
                Asteroid ast = (Asteroid) gObject;
                ast.updateAsteroid(gc, playerViewport.getSpaceship());
                if (ast.isIntersecting(playerViewport.getSpaceship())) {
                    //if asteroid is being mined, start sending updates to server 
                    //gameObjectDAO.sendData(ast);
                }
            }  else if (gObject instanceof Spaceship) {
                Spaceship spaceship = (Spaceship) gObject;
                spaceship.update(gc);
                //check planet collision && input key
            } else if (gObject instanceof Inventory) {
                Inventory inventory = (Inventory) gObject;
                if (!inventory.isAuctionHouse()) {
                    //System.out.println("--[GameWorldImpl]Set player Inventory");
                    //SpaceClient.playerInventory = inventory;
                } else {
                    //SpaceClient.auctionhouseInventory = inventory;
                    //System.out.println("--[GameWorldImpl]Set auctionhouse Inventory");
                }
            } else if (gObject instanceof Planet) {
                Planet planet = (Planet) gObject;
                //System.out.println("Planet Name: " + planet.getName());
                if (gc.getInput().isKeyPressed(Input.KEY_F2)) {
                    System.out.println("Current state: " + this.client.getCurrentStateId());
                    if (client.getCurrentStateId() != SpaceClient.STATE_PLANET) {
                        if (planet.getRectangle().intersects(playerViewport.getSpaceship().getGlobalRectangle())) {
                            client.toPlanetState();
                        }
                    }
                }
            }
        }
        chat.update(gc);
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
            }  else if (gObject instanceof Spaceship) {
                Spaceship spaceship2 = (Spaceship) gObject;
                spaceship2.render(g, false);
            } else if (gObject instanceof Planet) {
                Planet planet = (Planet) gObject;
                planet.render(g, false);
            }
        }
        minimap.render(g, playerViewport.getSpaceship(), gameObjects);
        chat.render(g, true);
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

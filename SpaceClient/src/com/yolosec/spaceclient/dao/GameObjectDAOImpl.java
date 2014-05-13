/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.dao;

import com.yolosec.spaceclient.dao.interfaces.GameObjectDAO;
import java.util.ArrayList;
import java.util.List;
import com.yolosec.spaceclient.dao.interfaces.DrawCallback;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
import com.yolosec.spaceclient.communication.BroadcastHandler;
import com.yolosec.spaceclient.game.player.Inventory;
import com.yolosec.spaceclient.game.world.Asteroid;
import com.yolosec.spaceclient.game.world.GameWorldImpl;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.game.world.Planet;
import com.yolosec.spaceclient.gui.SpaceClient;
import com.yolosec.spaceclient.observing.NodeImpl;

/**
 *
 * @author Toon
 */
public class GameObjectDAOImpl extends NodeImpl<GameWorldImpl> implements GameObjectDAO, DrawCallback {

    /**
     * Holds the GameObjects that are currently relevant for the GameWorld.
     */
    private List<GameObjectImpl> gameObjects;

    /**
     * Used for communication with the GameServer.
     */
    private BroadcastHandler broadcastHandler;
    private Thread broadcastHandlerThread;

    /**
     * Creates a new instance of type GameObjectDAOImpl.s Keeps track of all the
     * (relevant)GameObjects in the world. Receives updates from the server and
     * sends the to the GameWorldImpl and vice versa.
     */
    public GameObjectDAOImpl() {
        broadcastHandler = new BroadcastHandler(this);
        broadcastHandlerThread = new Thread(broadcastHandler);
        broadcastHandlerThread.start();
        this.gameObjects = new ArrayList<>();
    }

    /**
     * Sends a gameObject to the server.
     *
     * @param gameObject the GameObject to send.
     */
    public void sendData(GameObjectImpl gameObject) {
        broadcastHandler.sendData(gameObject);
    }

    @Override
    public List<GameObjectImpl> getGameObjects() {
        return gameObjects;
    }

    @Override
    public void setGameObjects(List<GameObjectImpl> objects) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void drawAfterDataReadFromSocketFromServer(List<GameObjectImpl> objects) {
        for (GameObjectImpl goi : objects) {
            if (goi instanceof Asteroid) {
                Asteroid ast = (Asteroid) goi;
                Asteroid existing = asteroidExists(ast);
                if (existing == null) {
                    //System.out.println("adding new asteroid");
                    gameObjects.add(goi);
                } else {
                    //update resource amount of asteroid
                    //check of resource amount has changed
                    if (ast.getResourceAmount() != existing.getResourceAmount()) {
                        //System.out.println("got updated asteroid");
                        if (ast.getResourceAmount() < existing.getResourceAmount()) {
                            existing.setResourceAmount(ast.getResourceAmount());
                        }
                    }
                }
            }
            if (goi instanceof Spaceship) {
                Spaceship spa = (Spaceship) goi;
                Spaceship existing = spaceshipExists(spa);
                if (existing == null) {
                    gameObjects.add(goi);
                } else {
                    //update position from spaceship
                    existing.setPosition(spa.getPosition());
                    existing.setResources(spa.getResources());
                }
            }
            if (goi instanceof Inventory) {
                Inventory inv = (Inventory) goi;
                Inventory existing = inventoryExists(inv);
                if (existing == null) {
                    SpaceClient.playerInventory = inv;
                    gameObjects.add(goi);
                }
            }
            
            if(goi instanceof Planet) {
                gameObjects.add(goi);
            }
        }
    }

    /**
     * *
     * Checks if a spaceship already exists in the local gameclient gameobject
     * list.
     *
     * @param ship the object to check
     * @return an instance of the ship if it exists, null otherwise.
     */
    private Spaceship spaceshipExists(Spaceship ship) {
        int id = ship.getId();
        for (GameObjectImpl goimpl : gameObjects) {
            if (goimpl instanceof Spaceship) {
                Spaceship compareShip = (Spaceship) goimpl;
                if (compareShip.getId() == id) {
                    return compareShip;
                }
            }
        }
        return null;
    }

    /**
     * *
     * Checks if an asteroid exists in the local gameclient gameobject list.
     *
     * @param ast the asteroid to check for
     * @return an instance of the asteroid if it exists, null otherwise.
     */
    private Asteroid asteroidExists(Asteroid ast) {
        float astX = ast.getXPosition();
        float astY = ast.getYPosition();

        for (GameObjectImpl goimpl : gameObjects) {
            if (goimpl instanceof Asteroid) {
                Asteroid compareAst = (Asteroid) goimpl;
                if (compareAst.getXPosition() == astX && compareAst.getYPosition() == astY) {
                    return compareAst;
                }
            }
        }
        return null;
    }

    private Inventory inventoryExists(Inventory in) {

        for (GameObjectImpl goimpl : gameObjects) {
            if (goimpl instanceof Inventory) {
                Inventory inventory = (Inventory) goimpl;
                if (inventory.getSpaceshipId() == in.getSpaceshipId()) {
                    return inventory;
                }
            }
        }
        return null;
    }

    @Override
    public List<Spaceship> getSpaceships() {
        List<Spaceship> spaceships = new ArrayList<>();
        for (GameObjectImpl gObject : gameObjects) {
            if (gObject instanceof Spaceship) {
                spaceships.add((Spaceship) gObject);
            }
        }

        return spaceships;
    }
}

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
import com.yolosec.spaceclient.game.world.Viewport;
import com.yolosec.spaceclient.gui.SpaceClient;
import com.yolosec.spaceclient.observing.NodeImpl;
import java.util.AbstractList;
import java.util.Iterator;

/**
 *
 * @author Toon
 */
public class GameObjectDAOImpl extends NodeImpl<GameWorldImpl> implements GameObjectDAO, DrawCallback {

    /**
     * Holds the GameObjects that are currently relevant for the GameWorld.
     */
    private List<GameObjectImpl> totalObjects;
//    private List<GameObjectImpl> asteroidsObjects;
//    private List<Spaceship> spaceShipObjects;
//    private List<GameObjectImpl> inventoryObjects;
//    private List<GameObjectImpl> planetsObjects;
    private int ownSpaceshipId;

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
        this.totalObjects = new ArrayList<>();
//        this.asteroidsObjects = new ArrayList<>();
//        this.spaceShipObjects = new ArrayList<>();
//        this.inventoryObjects = new ArrayList<>();
//        this.planetsObjects = new ArrayList<>();
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
        return totalObjects;
    }

    @Override
    public void setGameObjects(List<GameObjectImpl> objects) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void drawAfterDataReadFromSocketFromServer(List<GameObjectImpl> objects) {
        //filterGameObjects(objects);
        //System.out.println("---[GameObjectDAOImpl] drawAfterDataReadFromSocketFromServer");
        //spaceShipObjects.clear();
        //inventoryObjects.clear();
        for (GameObjectImpl goi : objects) {
            if (goi instanceof Asteroid) {
                Asteroid ast = (Asteroid) goi;
                Asteroid existing = asteroidExists(ast);
                if (existing == null) {
                    //System.out.println("adding new asteroid");
                    totalObjects.add(goi);
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
                    if (spa.getId() == Viewport.spaceship.getId()) {
                        int[] newResources = new int[3];
                        int temp = 0;
                        for (int rs : spa.getResources()) {
                            //if the new resource is higher then the current change the resource
                            if (rs > Viewport.spaceship.getResources()[temp]) {
                                newResources[temp] = rs;
                            } else {
                                newResources[temp] = Viewport.spaceship.getResources()[temp];
                            }
                            temp++;
                        }
                        Viewport.spaceship.setResources(newResources);
                    } else {
                        totalObjects.add(spa);
                    }
                } else {
                    //update position from spaceship
                    existing.setPosition(spa.getPosition());
                    existing.setResources(spa.getResources());
                    //System.out.println("--[GameObjesDAOImpl] old resources: " + existing.getResources()[0] + " -- new resources: " + spa.getResources()[0]);
                }
            }
            if (goi instanceof Inventory) {
                Inventory inv = (Inventory) goi;
                if (!inv.isAuctionHouse()) {
                    SpaceClient.playerInventory = inv;
                    //System.out.println("--[GameObjectDAOImpl] Set player inventory");
                } else {
                    SpaceClient.auctionhouseInventory = inv;
                    System.out.println("Auctionhouse items size: " + SpaceClient.auctionhouseInventory.getItems().size());
                    //System.out.println("--[GameObjectDAOImpl] Set auctionhouse inventory");
                }
                totalObjects.add(goi);
            }

            if (goi instanceof Planet) {
                totalObjects.add(goi);
            }
        }

    }

    private void filterGameObjects(List<GameObjectImpl> objects) {
        //new object
        Iterator it = totalObjects.iterator();
        //System.out.println("Size game objects while filtering: " + totalObjects.size());
        while (it.hasNext()) {
            GameObjectImpl object = (GameObjectImpl) it.next();
            if (object instanceof Asteroid) {
                it.remove();
                //System.out.println("Removed asteroid");
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
        for (GameObjectImpl goimpl : totalObjects) {
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

        for (GameObjectImpl goimpl : totalObjects) {
            if (goimpl instanceof Asteroid) {
                Asteroid compareAst = (Asteroid) goimpl;
                if (compareAst.getXPosition() == astX && compareAst.getYPosition() == astY) {
                    return compareAst;
                }
            }
        }
        return null;
    }

    @Override
    public List<Spaceship> getSpaceships() {
        List<Spaceship> spaceships = new ArrayList<>();
        for (GameObjectImpl gObject : totalObjects) {
            if (gObject instanceof Spaceship) {
                spaceships.add((Spaceship) gObject);
            }
        }

        return spaceships;
    }
}

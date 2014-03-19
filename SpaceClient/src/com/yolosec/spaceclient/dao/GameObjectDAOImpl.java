/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.dao;

import com.ptsesd.groepb.shared.AsteroidType;
import com.yolosec.spaceclient.dao.interfaces.GameObjectDAO;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.yolosec.spaceclient.dao.interfaces.DrawCallback;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
import com.yolosec.spaceclient.communication.BroadcastHandler;
import com.yolosec.spaceclient.communication.Communicator;
import com.yolosec.spaceclient.game.world.Asteroid;
import com.yolosec.spaceclient.game.world.GameWorldImpl;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.observing.NodeImpl;

/**
 *
 * @author Toon
 */
public class GameObjectDAOImpl extends NodeImpl<GameWorldImpl> implements GameObjectDAO, DrawCallback {

    private List<GameObjectImpl> gameObjects;
    private BroadcastHandler broadcastHandler;

    public GameObjectDAOImpl() {
        try {
            Communicator.initiate();
            broadcastHandler = new BroadcastHandler(this);

            Thread th = new Thread(broadcastHandler);
            th.start();

            this.gameObjects = new ArrayList<>();
            Random r = new Random();
            for (int i = 0; i < 20; i++) {
                AsteroidType t;
                if (r.nextInt() % 2 == 0) {
                    t = AsteroidType.common;
                } else if (r.nextInt() % 3 == 0) {
                    t = AsteroidType.magic;
                } else {
                    t = AsteroidType.rare;
                }
                Asteroid ast = new Asteroid(r.nextInt(1920), r.nextInt(1080), 100, t);
                //this.gameObjects.add(ast);
            }
        } catch (SocketException ex) {
            Logger.getLogger(GameObjectDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void sendData(GameObjectImpl gameObject) {
        broadcastHandler.sendData(gameObject);
    }

    public void login(String username, String password) {
        broadcastHandler.login(username, password);
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
        if (objects != null && objects.size() > 0) {
            gameObjects = objects;
        }
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceclient.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import shared.AsteroidType;
import spaceclient.dao.interfaces.DrawCallback;
import shared.GameObject;
import spaceclient.game.Asteroid;
import spaceclient.game.Spaceship;

/**
 *
 * @author Toon
 */
public class GameObjectDAOImpl implements GameObjectDAO, DrawCallback {

    private List<GameObject> gameObjects;

    public GameObjectDAOImpl() {
        this.gameObjects = new ArrayList<>();
        Random r = new Random();
        for(int i = 0; i < 100; i++) {
            Asteroid ast = new Asteroid(r.nextInt(1920), r.nextInt(1080), 100, AsteroidType.common);
            this.gameObjects.add(ast);
        }
    }
    
    @Override
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    @Override
    public void setGameObjects(List<GameObject> objects) {
    }

    @Override
    public void drawAfterDataReadFromSocketFromServer(List<GameObject> objects) {
        if(objects != null && objects.size() > 0) {
            gameObjects = objects;
        }
    }

    @Override
    public List<Spaceship> getSpaceships() {
        List<Spaceship> spaceships = new ArrayList<>();
        for(GameObject gObject : gameObjects) {
            if(gObject instanceof Spaceship) {
                spaceships.add((Spaceship)gObject);
            }
        }
        
        return spaceships;
    }
    
}

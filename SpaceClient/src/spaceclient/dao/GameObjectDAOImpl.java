/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceclient.dao;

import java.util.ArrayList;
import java.util.List;
import spaceclient.game.GameObject;

/**
 *
 * @author Toon
 */
public class GameObjectDAOImpl implements GameObjectDAO{

    private List<GameObject> gameObjects;

    public GameObjectDAOImpl() {
        this.gameObjects = new ArrayList<>();
    }
    
    @Override
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    @Override
    public void setGameObjects(List<GameObject> objects) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

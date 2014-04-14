/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.dao.interfaces;

import com.yolosec.spaceclient.dao.interfaces.GameObject;
import java.util.List;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
import com.yolosec.spaceclient.game.world.GameWorldImpl;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.observing.Node;

/**
 *
 * @author Toon
 */
public interface GameObjectDAO extends Node<GameWorldImpl> {

    List<GameObjectImpl> getGameObjects();

    void setGameObjects(List<GameObjectImpl> objects);

    List<Spaceship> getSpaceships();
}

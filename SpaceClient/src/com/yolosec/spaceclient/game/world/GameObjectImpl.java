/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game.world;

import com.yolosec.spaceclient.dao.interfaces.GameObjectDAO;
import com.yolosec.spaceclient.dao.interfaces.GameObject;
import com.yolosec.spaceclient.observing.NodeImpl;
import java.awt.Rectangle;

/**
 * The abstract implementation for GameObject
 * @author Toon
 */
public abstract class GameObjectImpl extends NodeImpl<GameObjectDAO> implements GameObject {

    /**
     * 
     * @param object the GameObject to check collision with.
     * @return true if the two GameObjects are colliding, false otherwise.
     */
    public boolean isIntersecting(GameObjectImpl object) {
        return this.getRectangle().intersects(object.getRectangle());
    }

    /**
     * 
     * @return the (bounding) Rectangle for this GameObject.
     */
    public abstract Rectangle getRectangle();
}

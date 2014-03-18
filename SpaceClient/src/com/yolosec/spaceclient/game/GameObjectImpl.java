/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game;

import com.yolosec.spaceclient.dao.GameObjectDAO;
import com.yolosec.spaceclient.dao.interfaces.GameObject;
import com.yolosec.spaceclient.observing.NodeImpl;
import java.awt.Rectangle;

/**
 *
 * @author Toon
 */
public abstract class GameObjectImpl extends NodeImpl<GameObjectDAO> implements GameObject {

    public boolean isIntersecting(GameObjectImpl object) {
        return this.getRectangle().intersects(object.getRectangle());
    }

    public abstract Rectangle getRectangle();
}

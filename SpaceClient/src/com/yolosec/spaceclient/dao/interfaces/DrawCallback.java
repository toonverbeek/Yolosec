/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yolosec.spaceclient.dao.interfaces;

import java.util.List;
import com.yolosec.spaceclient.game.world.GameObjectImpl;

/**
 *
 * @author Tim
 */
public interface DrawCallback {
    void drawAfterDataReadFromSocketFromServer(List<GameObjectImpl> objects);
}

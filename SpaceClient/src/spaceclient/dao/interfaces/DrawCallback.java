/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceclient.dao.interfaces;

import java.util.List;
import java.util.Map;
import shared.GameObject;
import spaceclient.game.Spaceship;

/**
 *
 * @author Tim
 */
public interface DrawCallback {
    void drawAfterDataReadFromSocketFromServer(List<GameObject> objects);
}

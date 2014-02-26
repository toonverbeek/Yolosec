/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceclient.dao.interfaces;

import spaceclient.communication.SpaceshipComm;
import spaceclient.game.Spaceship;

/**
 *
 * @author Tim
 */
public interface DrawCallback {
    void drawAfterDataReadFromSocketFromServer(Spaceship sShip);
}

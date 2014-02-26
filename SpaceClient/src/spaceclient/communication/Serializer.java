/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceclient.communication;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import spaceclient.game.Spaceship;

/**
 *
 * @author Tim
 */
public class Serializer {
    
    private static Gson gson = new Gson();
    
    public static String serializeSpaceship(Spaceship spaceship) {
        SpaceshipComm sComm = new SpaceshipComm(spaceship);
        String json = gson.toJson(sComm, SpaceshipComm.class);
        return json;
    }
    
    public static Spaceship deserializeSpaceship(String json) {
        SpaceshipComm sComm = (SpaceshipComm) gson.fromJson(json, SpaceshipComm.class);
        Spaceship sShip = new Spaceship(50, 50, new Rectangle(0, 0, 50, 50));
        sShip.setPosition(new Vector2f(sComm.getX(), sComm.getY()));
        sShip.setDirection(sComm.getDirection());
        return sShip;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceclient.communication;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
}

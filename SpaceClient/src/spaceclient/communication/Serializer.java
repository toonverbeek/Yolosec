/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.communication;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map.Entry;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import spaceclient.game.GameObject;
import spaceclient.game.Spaceship;

/**
 *
 * @author Tim
 */
public class Serializer {

    private static final Gson gson = new Gson();

    public static String serializeSpaceship(Spaceship spaceship) {
        SpaceshipComm sComm = new SpaceshipComm(SpaceshipComm.class.getSimpleName(), spaceship);
        String json = gson.toJson(sComm, SpaceshipComm.class);
        System.out.println(json);
        return json;
    }

    public static GameObject desirializePacket(JsonElement json) {
        GamePacket gp = (GamePacket) gson.fromJson(json, GamePacket.class);
        JsonObject jobject = json.getAsJsonObject();
        Entry<String, JsonElement> entry = jobject.entrySet().iterator().next();
        
        System.out.println("Header: " + gp.getHeader());
        System.out.println("JSON entry: " + entry.getKey());
        switch (entry.getKey()) {
            case "SpaceshipComm":
                Type listOfTestObject = new TypeToken<List<GamePacket>>() {
                }.getType();
                List<GamePacket> list2 = gson.fromJson(json, listOfTestObject);
                return deserializeSpaceship(json);
            default:
                return null;
        }
    }

    private static Spaceship deserializeSpaceship(JsonElement json) {
        SpaceshipComm sComm = (SpaceshipComm) gson.fromJson(json, SpaceshipComm.class);
        Spaceship sShip = new Spaceship(50, 50, new Rectangle(0, 0, 50, 50));
        sShip.setPosition(new Vector2f(sComm.getX(), sComm.getY()));
        sShip.setDirection(sComm.getDirection());
        return sShip;
    }
}

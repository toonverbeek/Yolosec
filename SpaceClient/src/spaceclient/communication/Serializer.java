/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.communication;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import shared.GamePacket;
import shared.SpaceshipComm;
import spaceclient.game.GameObject;
import spaceclient.game.Spaceship;

/**
 *
 * @author Tim
 */
public class Serializer {

    private static final Gson gson = new Gson();

    public static String serializeSpaceship(Spaceship spaceship) {
        System.out.println("serialize reached");
        SpaceshipComm sComm = new SpaceshipComm(SpaceshipComm.class.getSimpleName(), spaceship.getPosition().x, spaceship.getPosition().y, 1, spaceship.getId());
        String json = gson.toJson(sComm, SpaceshipComm.class);
        System.out.println(json);
        return json;
    }

    public static String serializeLogin(LoginComm lc) {
        return gson.toJson(lc, LoginComm.class);
    }

    public static List<GameObject> deserializePackets(JsonReader reader) throws IOException {
        List<GameObject> gameobjects = new ArrayList<>();
        if (reader.hasNext()) {
            List<Map> retrievedObjects = gson.fromJson(reader, List.class);
            System.out.println("deserializePackets: " + retrievedObjects);

            for (Map map : retrievedObjects) {
                System.out.println(map.get("header"));
                int id = ((Double) map.get("id")).intValue();
                double x = (Double) map.get("x");
                double y = (Double) map.get("y");
                float xx = (float) x;
                float yy = (float) y;
                int d = ((Double) map.get("d")).intValue();
                SpaceshipComm scomm = new SpaceshipComm((String) map.get("header"), xx, yy, d, id);
                gameobjects.add(deserializeSpaceship(scomm));
            }
        }
        return gameobjects;
    }

    private static Spaceship deserializeSpaceship(SpaceshipComm scomm) {
        System.out.println("deserializing scomm to spaceship");
        Spaceship sShip = new Spaceship(50, 50, new Rectangle(0, 0, 50, 50));
        sShip.setPosition(new Vector2f(scomm.getX(), scomm.getY()));
        sShip.setDirection(scomm.getDirection());
        return sShip;
    }
}

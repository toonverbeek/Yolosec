/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author Tim
 */
public class Serializer {

    private static final Gson gson = new Gson();

//    public static String serializeSpaceship(Spaceship spaceship) {
//        SpaceshipComm sComm = new SpaceshipComm(SpaceshipComm.class.getSimpleName(), spaceship.getPosition().x, spaceship.getPosition().y, 1, spaceship.getId(), spaceship.getResources());
//        String json = gson.toJson(sComm, SpaceshipComm.class);
//        return json;
//    }

    public static String serializeLogin(LoginComm lc) {
        return gson.toJson(lc, LoginComm.class);
    }

    public static List<GamePacket> deserializePackets(JsonReader reader) throws IOException {
        List<GamePacket> gameobjects = new ArrayList<>();
        if (reader.hasNext()) {
            List<Map> retrievedObjects = gson.fromJson(reader, List.class);

            for (Map map : retrievedObjects) {
                String header = (String) map.get("header");
                if (header.equals(SpaceshipComm.class.getSimpleName())) {
                    int id = ((Double) map.get("id")).intValue();
                    double x = (Double) map.get("x");
                    double y = (Double) map.get("y");
                    float xx = (float) x;
                    float yy = (float) y;
                    int d = ((Double) map.get("d")).intValue();
                    int[] resources = (int[]) map.get("resources");
                    SpaceshipComm scomm = new SpaceshipComm((String) map.get("header"), xx, yy, d, id, resources);
                    gameobjects.add(scomm);
                } else if (header.equals(AsteroidComm.class.getSimpleName())) {
                    //deserialize asteroidcomm
                }
            }
        }
        return gameobjects;
    }

//    private static Spaceship deserializeSpaceship(SpaceshipComm scomm) {
//        Spaceship sShip = new Spaceship(10, 10);
//        sShip.setPosition(new Vector2f(scomm.getX(), scomm.getY()));
//        sShip.setDirection(scomm.getDirection());
//        return sShip;
//    }

//    private static Asteroid deserializeAsAsteroid(AsteroidComm acom) {
//        return new Asteroid(acom);
//    }

//    public static String serializeAsteroid(Asteroid asteroid) {
//        AsteroidComm aCom = new AsteroidComm(AsteroidComm.class.getSimpleName(), asteroid.getType(), asteroid.getResourceAmount(), asteroid.getX(), asteroid.getY());
//        String json = gson.toJson(aCom, AsteroidComm.class);
//        return json;
//    }

    public static String serializeSpaceShipAsGamePacket(String header, float x, float y, int direction, int id, int[] resources) {
        SpaceshipComm sComm = new SpaceshipComm(header, x, y, direction, id, resources);
        String json = gson.toJson(sComm, SpaceshipComm.class);
        return json;
    }

    public static String serializeAsteroidAsGamePacket(String header, AsteroidType type, int resourceAmount, int x, int y) {
        AsteroidComm aCom = new AsteroidComm(header, type, resourceAmount, x, y);
        String json = gson.toJson(aCom, AsteroidComm.class);
        return json;
    }
}

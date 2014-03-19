/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptsesd.groepb.shared;;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tim
 */
public class Serializer {

    private static final Gson gson = new Gson();

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
                    //desirialize spaceshipcomm
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
                    int resourceAmount = ((Double) map.get("resourceAmount")).intValue();
                    float x = ((Double) map.get("x")).floatValue();
                    float y = ((Double) map.get("y")).floatValue();
                    AsteroidType type = (AsteroidType)map.get("type");
                    AsteroidComm ac = new AsteroidComm(AsteroidComm.class.getSimpleName(), type, resourceAmount, x, y);
                    gameobjects.add(ac);
                } else if (header.equals(LoginComm.class.getSimpleName())) {
                    String username = (String) map.get("username");
                    String password = (String) map.get("password");
                    LoginComm lcomm = new LoginComm(LoginComm.class.getSimpleName(), username, password);
                    gameobjects.add(lcomm);
                }
            }
        }
        return gameobjects;
    }


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

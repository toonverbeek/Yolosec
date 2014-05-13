package com.ptsesd.groepb.shared;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ptsesd.groepb.shared.socket.AuctionRequest;
import com.ptsesd.groepb.shared.socket.InventoryReply;
import com.ptsesd.groepb.shared.socket.InventoryRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tim
 */
public class Serializer {

    private static final Gson gson = new Gson();
    private static int nextIdInt = 0;

    public static String serializeLogin(LoginComm lc) {
        return gson.toJson(lc, LoginComm.class);
    }

    public synchronized static int getNextGamePacketId() {
        nextIdInt++;
        return nextIdInt;
    }

    public synchronized static void setNextGamePacketId(int nextIdInt) {
        Serializer.nextIdInt = nextIdInt;
    }

    public static GamePacket getSingleGamePacket(JsonReader reader) throws IOException {
        GamePacket gameobject = null;
        if (reader.hasNext()) {

            Map map = gson.fromJson(reader, Map.class);

            String header = (String) map.get("header");

            if (header.equals(SpaceshipComm.class.getSimpleName())) {
                //desirialize spaceshipcomm
                int id = ((Double) map.get("id")).intValue();
                double x = (Double) map.get("x");
                double y = (Double) map.get("y");
                float xx = (float) x;
                float yy = (float) y;
                int d = ((Double) map.get("d")).intValue();
                List<Double> resourcesList = (ArrayList<Double>) map.get("resources");
                int[] resources = convertIntegers(resourcesList);

                boolean mining = (boolean) map.get("mining");
                SpaceshipComm scomm = new SpaceshipComm((String) map.get("header"), id, xx, yy, d, resources, mining);
                gameobject = scomm;
            } else if (header.equals(AsteroidComm.class.getSimpleName())) {
                //deserialize asteroidcomm
                int resourceAmount = ((Double) map.get("resourceAmount")).intValue();
                float x = ((Double) map.get("x")).floatValue();
                float y = ((Double) map.get("y")).floatValue();
                AsteroidType atype = AsteroidType.common;
                String type = (String) map.get("type");
                if (type.equals(AsteroidType.magic.toString())) {
                    atype = AsteroidType.magic;
                } else if (type.equals(AsteroidType.rare.toString())) {
                    atype = AsteroidType.rare;
                }
                AsteroidComm ac = new AsteroidComm(AsteroidComm.class.getSimpleName(), atype, resourceAmount, x, y);
                gameobject = ac;
            } else if (header.equals(LoginComm.class.getSimpleName())) {
                String username = (String) map.get("username");
                String password = (String) map.get("password");
                LoginComm lcomm = new LoginComm(LoginComm.class.getSimpleName(), username, password);
                gameobject = lcomm;
            } else if (header.equals(LoginCommError.class.getSimpleName())) {
                gameobject = new LoginCommError(LoginCommError.class.getSimpleName());
            } else if (header.equals(InventoryRequest.class.getSimpleName())) {
                Integer spaceshipId = ((Double) map.get("spaceshipId")).intValue();
                InventoryRequest inventoryRequest = new InventoryRequest(InventoryRequest.class.getSimpleName(), spaceshipId);
                gameobject = inventoryRequest;
            } else if (header.equals(InventoryReply.class.getSimpleName())) {
                Integer spaceshipId = ((Double) map.get("spaceshipId")).intValue();
                List<ItemComm> items = new ArrayList<>((List<ItemComm>) map.get("items"));
                InventoryReply inventoryReply = new InventoryReply(InventoryReply.class.getSimpleName(), spaceshipId, items);
                gameobject = inventoryReply;
            } else if (header.equals(AuctionRequest.class.getSimpleName())){
                Integer userID = ((Double) map.get("userId")).intValue();
                Integer itemId = ((Double) map.get("itemId")).intValue();
                AuctionHouseRequestType requestType = (AuctionHouseRequestType) map.get("type");
                AuctionRequest request = new AuctionRequest(header, userID, itemId, requestType);
                gameobject = request;
            }

        }
        return gameobject;
    }

    public static int[] convertIntegers(List<Double> integers) {
        int[] ret = new int[integers.size()];

        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
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
                    List<Double> resourcesList = (ArrayList<Double>) map.get("resources");
                    int[] resources = convertIntegers(resourcesList);
                    boolean mining = (boolean) map.get("mining");
                    SpaceshipComm scomm = new SpaceshipComm((String) map.get("header"), id, xx, yy, d, resources, mining);
                    gameobjects.add(scomm);
                } else if (header.equals(AsteroidComm.class.getSimpleName())) {
                    //deserialize asteroidcomm
                    int resourceAmount = ((Double) map.get("resourceAmount")).intValue();
                    float x = ((Double) map.get("x")).floatValue();
                    float y = ((Double) map.get("y")).floatValue();
                    AsteroidType type = (AsteroidType) map.get("type");
                    AsteroidComm ac = new AsteroidComm(AsteroidComm.class.getSimpleName(), type, resourceAmount, x, y);
                    gameobjects.add(ac);
                } else if (header.equals(LoginComm.class.getSimpleName())) {
                    String username = (String) map.get("username");
                    String password = (String) map.get("password");
                    LoginComm lcomm = new LoginComm(LoginComm.class.getSimpleName(), username, password);
                    gameobjects.add(lcomm);
                } else if (header.equals(MessagingComm.class.getSimpleName())) {
                    Integer spaceshipid = ((Double) map.get("spaceShipId")).intValue();
                    String username = (String) map.get("username");
                    String message = (String) map.get("message");
                    Date timestamp = (Date) map.get("timestamp");
                    MessagingComm mComm = new MessagingComm(MessagingComm.class.getSimpleName(), spaceshipid, message, username);
                    mComm.setTimestamp(); //sets timestamp to now
                    gameobjects.add(mComm);
                } else if (header.equals(LoginCommError.class.getSimpleName())) {
                    gameobjects.add(new LoginCommError(LoginCommError.class.getSimpleName()));
                } else if (header.equals(InventoryRequest.class.getSimpleName())) {
                    Integer spaceshipId = ((Double) map.get("spaceshipId")).intValue();
                    gameobjects.add(new InventoryRequest(InventoryRequest.class.getSimpleName(), spaceshipId));
                } else if (header.equals(InventoryReply.class.getSimpleName())) {
                    Integer spaceshipId = ((Double) map.get("spaceshipId")).intValue();
                    List<ItemComm> items = new ArrayList<>((List<ItemComm>) map.get("items"));
                    gameobjects.add(new InventoryReply(InventoryReply.class.getSimpleName(), spaceshipId, items));
                } else if (header.equals(PlanetComm.class.getSimpleName())) {
                    int size = ((Double) map.get("size")).intValue();
                    float x = ((Double) map.get("x")).floatValue();
                    float y = ((Double) map.get("y")).floatValue();
                    String planetname = (String) map.get("planetName");
                    PlanetComm pcomm = new PlanetComm(header, planetname, size, x, y);
                    gameobjects.add(pcomm);
                }

            }
        }
        return gameobjects;
    }

    public static String serializeSpaceShipAsGamePacket(String header, Integer id, float x, float y, int direction, int[] resources, boolean mining) {
        SpaceshipComm sComm = new SpaceshipComm(header, id, x, y, direction, resources, mining);
        String json = gson.toJson(sComm, SpaceshipComm.class);
        return json;
    }

    public static String serializeAsteroidAsGamePacket(String header, AsteroidType type, int resourceAmount, int x, int y) {
        AsteroidComm aCom = new AsteroidComm(header, type, resourceAmount, x, y);
        String json = gson.toJson(aCom, AsteroidComm.class);
        return json;
    }

    public static String serializeAsteroidAsGamePackets(List<AsteroidComm> asteroids) {
        Type com = new TypeToken<List<GamePacket>>() {
        }.getType();
        String json = gson.toJson(asteroids, com);
        return json;
    }

    public static String serializeSpaceShipAsGamePackets(List<SpaceshipComm> ships) {
        Type com = new TypeToken<List<GamePacket>>() {
        }.getType();
        String json = gson.toJson(ships, com);
        return json;
    }

    public static String serializeLoginCommErrorAsGamePacktet(LoginCommError err) {
        String json = gson.toJson(err, LoginCommError.class);
        return json;
    }

    public static String serializeMessageAsGamePacket(MessagingComm mCom) {
        String json = gson.toJson(mCom, MessagingComm.class);
        return json;
    }

    public static String serializeInventoryRequestAsGamePacktet(InventoryRequest req) {
        String json = gson.toJson(req, InventoryRequest.class);
        return json;
    }

    public static String serializeInventoryReplyAsGamePacktet(InventoryReply rep) {
        String json = gson.toJson(rep, InventoryReply.class);
        return json;
    }

    public static String serializeAuctionRequestAsGamePacktet(AuctionRequest auctionRequest) {
        String json = gson.toJson(auctionRequest, AuctionRequest.class);
        return json;
    }
    public static String serializePlanetAsGamePacket(String header, String planetname, int size, float x, float y) {
        PlanetComm pcom = new PlanetComm(header, planetname, size, x, y);
        String json = gson.toJson(pcom, PlanetComm.class);
        return json;
    }
}

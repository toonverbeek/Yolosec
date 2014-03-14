package com.modules;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.GamePacket;
import shared.SpaceshipComm;

/**
 *
 * @author Administrator
 */
public class PlayerLocationModule {

    private final Gson gson;
    private Map<ClientConnection, SpaceshipComm> clientSpaceships;

    public PlayerLocationModule() {
        gson = new Gson();
        this.clientSpaceships = new HashMap<>();
    }
    
    public void addSpaceship(SpaceshipComm newShip, ClientConnection connection){
        if(!clientSpaceships.containsValue(newShip)){
            System.out.println("added spaceship");
            clientSpaceships.put(connection, newShip);
        }
    }
    
    public Map<ClientConnection, SpaceshipComm> getClientSpaceships() {
        return clientSpaceships;
    }

    private List<SpaceshipComm> getAllPositions(SpaceshipComm requestor) {
        List<SpaceshipComm> positions = new ArrayList<>();

        for (Map.Entry<ClientConnection, SpaceshipComm> sender : this.clientSpaceships.entrySet()) {
            //If the same as the requestor space ship ignore
            if (requestor != sender.getValue()) {
                positions.add(sender.getValue());
            }
        }
        
        return positions;
    }

    public void sendPositions() {
        for (Map.Entry<ClientConnection, SpaceshipComm> client : this.clientSpaceships.entrySet()) {
            try {
                if (client.getKey() != null) {
                    //the writer which needs to send a list of positions
                    PrintWriter writer = new PrintWriter(client.getKey().getSocket().getOutputStream(), true);
                    
                    List<SpaceshipComm> positions = getAllPositions(client.getValue());
                    //System.out.println(String.format("Broadcasted positions %s", positions));
                    
                    Type com = new TypeToken<List<GamePacket>>(){}.getType();
                    String json = gson.toJson(positions, com);
                    writer.println(json);
                }
            } catch (IOException ex) {
                Logger.getLogger(PlayerLocationModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void updateSpaceship(int spaceshipID, float x, float y, int direction) {
        //System.out.println(String.format("%s %s %s %s ", new Object[]{spaceshipID,x , y, direction}));
        for (Map.Entry<ClientConnection, SpaceshipComm> client : this.clientSpaceships.entrySet()) {
            if (client.getValue().getId() == spaceshipID) {
                SpaceshipComm updateShip = client.getValue();
                updateShip.setX(x);
                updateShip.setY(y);
                updateShip.setDirection(direction);
            }
        }
    }
    
    public void logout(ClientConnection connection) {
        if(this.clientSpaceships.containsKey(connection)){
            this.clientSpaceships.remove(connection);
        }
    }

    public SpaceshipComm getClientSpaceship(ClientConnection conn) {
        if(this.clientSpaceships.containsKey(conn)){
            return this.clientSpaceships.get(conn);
        }
        return null;
    }
}

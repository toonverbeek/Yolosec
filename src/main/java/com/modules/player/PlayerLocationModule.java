package com.modules.player;

import com.server.ClientConnection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ptsesd.groepb.shared.GamePacket;
import com.ptsesd.groepb.shared.SpaceshipComm;

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
    
    public List<ClientConnection> getClientConnections(){
        return (List<ClientConnection>) clientSpaceships.keySet();
    }
    
    public Map<ClientConnection, SpaceshipComm> getClientSpaceships() {
        return clientSpaceships;
    }
    
    /**
     * Add a spaceship which is logged in, to retrieve and send position
     * @param newShip the new spaceship for communication
     * @param connection the connection which belongs to this spaceship
     */
    public void addSpaceship(SpaceshipComm newShip, ClientConnection connection){
        if(!clientSpaceships.containsValue(newShip)){
            clientSpaceships.put(connection, newShip);
        }
    }
    
    /**
     * Get all the positions from the connected spaceships
     * @param requestor
     * @return 
     */
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
    
    /**
     * Send all the positions
     */
    public void sendPositions() {
        //For every client connected
        for (Map.Entry<ClientConnection, SpaceshipComm> client : this.clientSpaceships.entrySet()) {
            try {
                //If the client exists
                if (client.getKey() != null) {
                    //the writer which needs to send a list of positions
                    PrintWriter writer = new PrintWriter(client.getKey().getSocket().getOutputStream(), true);
                    
                    //get all the positions
                    List<SpaceshipComm> positions = getAllPositions(client.getValue());
                    
                    //convert to a JSON serializable object
                    Type com = new TypeToken<List<GamePacket>>(){}.getType();
                    String json = gson.toJson(positions, com);
                    
                    //send the object
                    writer.println(json);
                }
            } catch (IOException ex) {
                System.err.println(String.format("IOException in PlayerLocationModule.sendPositions() - %s", ex.getMessage()));
            }
        }
    }
    
    /**
     * Update the current location of a spaceship
     * @param ship 
     */
    public void updateSpaceship(SpaceshipComm ship) {
        //System.out.println(String.format("%s %s %s %s ", new Object[]{spaceshipID,x , y, direction}));
        for (Map.Entry<ClientConnection, SpaceshipComm> client : this.clientSpaceships.entrySet()) {
            if (client.getValue().getId() == ship.getId()) {
                SpaceshipComm updateShip = client.getValue();
                updateShip.setX(ship.getX());
                updateShip.setY(ship.getY());
                updateShip.setDirection(ship.getDirection());
            }
        }
    }
    
    /**
     * Clean up when a player is logged out
     * @param connection 
     */
    public void logout(ClientConnection connection) {
        if(this.clientSpaceships.containsKey(connection)){
            this.clientSpaceships.remove(connection);
        }
    }

    /**
     * Find a single spaceship by a connection
     * @param conn
     * @return 
     */
    public SpaceshipComm getClientSpaceship(ClientConnection conn) {
        if(this.clientSpaceships.containsKey(conn)){
            return this.clientSpaceships.get(conn);
        }
        return null;
    }
}

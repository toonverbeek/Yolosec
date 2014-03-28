package com.modules.player;

import com.server.ClientConnection;

import com.google.gson.Gson;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.AsteroidType;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.SpaceshipComm;
import com.server.ConnectionServer;

/**
 *
 * @author Administrator
 */
public class SpaceshipCommModule {

    private Map<ClientConnection, SpaceshipComm> clientSpaceships;
    private ConnectionServer serverModule;

    public SpaceshipCommModule(ConnectionServer server) {
        this.serverModule = server;
        this.clientSpaceships = new HashMap<>();
    }
    
    /**
     * 
     * @return List<ClientConnection>
     */
    public List<ClientConnection> getClientConnections() {
        System.out.println("---[SERVER INFO] ClientConnection amount : " + clientSpaceships.size());
        return new ArrayList<>(clientSpaceships.keySet());
    }
    
    /**
     * @return a Map<ClientConnection, SpaceshipComm>
     */
    public Map<ClientConnection, SpaceshipComm> getClientSpaceships() {
        return clientSpaceships;
    }
    
    /**
     * Find a single spaceship by a connection
     *
     * @param conn
     * @return
     */
    public SpaceshipComm getSpaceshipFromClient(ClientConnection conn) {
        if (this.clientSpaceships.containsKey(conn)) {
            return this.clientSpaceships.get(conn);
        }
        return null;
    }
    
    /**
     * Get all the positions from the connected spaceships
     *
     * @param requestor
     * @return
     */
    private List<SpaceshipComm> getAllSpaceshipComms(SpaceshipComm requestor) {
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
     * Add a spaceship which is logged in, to retrieve and send position
     *
     * @param newShip the new spaceship for communication
     * @param connection the connection which belongs to this spaceship
     */
    public void addSpaceshipComm(SpaceshipComm newShip, ClientConnection connection) {
        if (!clientSpaceships.containsValue(newShip)) {
            clientSpaceships.put(connection, newShip);
        }
    }
    
    /**
     * remove a SpaceshipComm
     *
     * @param connection
     */
    public void removeSpaceshipComm(ClientConnection connection) {
        if (this.clientSpaceships.containsKey(connection)) {
            this.clientSpaceships.remove(connection);
        }
    }
    
    /**
     * send all the current asteroids
     */
    public void sendAsteroidComms() {
        List<AsteroidComm> asteroids = serverModule.getAsteroids();
        PrintWriter writer = null;
        for (Map.Entry<ClientConnection, SpaceshipComm> client : this.clientSpaceships.entrySet()) {
            try {
                writer = new PrintWriter(client.getKey().getSocket().getOutputStream(), true);

                for (AsteroidComm asteroidComm : asteroids) {
                    String aster = Serializer.serializeAsteroidAsGamePacket(asteroidComm.getHeader(), asteroidComm.getType(), asteroidComm.getResourceAmount(), (int) asteroidComm.getX(), (int) asteroidComm.getY());
                    //System.out.println("Aster" + aster);
                    writer.println(aster);
                }
            } catch (IOException ex) {
                System.err.println(String.format("Error in PlayerLocationModule.broadcastAsteroids() - %s", ex.getMessage()));
            }
        }
    }

    /**
     * Send all the SpaceshipComm objects
     */
    public void sendSpaceshipComms() {
        //For every client connected
        for (Map.Entry<ClientConnection, SpaceshipComm> client : this.clientSpaceships.entrySet()) {
            try {
                if (client != null) {
                    PrintWriter writer = new PrintWriter(client.getKey().getSocket().getOutputStream(), true);
                    //get all the positions, except for the clients own person
                    List<SpaceshipComm> positions = getAllSpaceshipComms(client.getValue());
                    
                    //send all SpaceshipComm projects
                    for(SpaceshipComm pos : positions){
                        //Serialize
                        String json = Serializer.serializeSpaceShipAsGamePacket(pos.getHeader(), pos.getId(), pos.getX(), pos.getY(), pos.getDirection(), pos.getResources(), pos.isMining());
                        //Send
                        writer.println(json);
                    }
                }
            } catch (IOException ex) {
                System.err.println(String.format("IOException in PlayerLocationModule.sendPositions() - %s", ex.getMessage()));
            }
        }
    }

    /**
     * Update the current location of a spaceship
     *
     * @param ship
     */
    public void updateSpaceshipComm(SpaceshipComm ship) {
        //System.out.println(String.format("%s %s %s %s ", new Object[]{spaceshipID,x , y, direction}));
        for (Map.Entry<ClientConnection, SpaceshipComm> client : this.clientSpaceships.entrySet()) {
            if (client.getValue().getId() == ship.getId()) {
                SpaceshipComm updateShip = client.getValue();
                updateShip.setX(ship.getX());
                updateShip.setY(ship.getY());
                updateShip.setDirection(ship.getDirection());
                updateShip.setResources(ship.getResources());
            }
        }
    }

    /**
     * 
     * @param ship
     * @param conn 
     */
    public void sendSpaceshipComm(SpaceshipComm ship, ClientConnection conn) {
        try {
            PrintWriter writer = new PrintWriter(conn.getSocket().getOutputStream(), true);
            String json = Serializer.serializeSpaceShipAsGamePacket(ship.getHeader(), ship.getId(), ship.getX(), ship.getY(), ship.getDirection(), ship.getResources(), ship.isMining());
            
            //Send
            writer.println(json);
        } catch (IOException ex) {
            System.err.println(String.format("IOException in PlayerLocationModule.sendSpaceshipComm() - %s", ex.getMessage()));
        }
    }
}

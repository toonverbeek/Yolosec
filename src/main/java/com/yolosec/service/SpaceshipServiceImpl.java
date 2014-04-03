package com.yolosec.service;

import com.yolosec.domain.GameClient;

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
import com.yolosec.data.SpaceshipDAO;
import com.yolosec.data.SpaceshipDAOImpl;
import com.yolosec.service.GameService;
import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public class SpaceshipServiceImpl implements SpaceshipService{

    //Map with spaceshipId, SpaceshiComm object
    private Map<Integer, SpaceshipComm> clientSpaceships;
    
    private SpaceshipDAO spaceshipDAO;

    public SpaceshipServiceImpl(GameService server) throws SQLException, ClassNotFoundException {
        this.clientSpaceships = new HashMap<>();
        this.spaceshipDAO = new SpaceshipDAOImpl();
    }
    
    @Override
    public void addOnlineSpaceship(SpaceshipComm ship) {
        if (!clientSpaceships.containsKey(ship.getId())) {
            clientSpaceships.put(ship.getId(), ship);
        }
    }

    @Override
    public void removeOnlineSpaceship(int spaceshipId) {
        if (!clientSpaceships.containsKey(spaceshipId)) {
            clientSpaceships.remove(spaceshipId);
        }
    }

    @Override
    public SpaceshipComm getOnlineSpaceship(int spaceshipId) {
        if (this.clientSpaceships.containsKey(spaceshipId)) {
            return this.clientSpaceships.get(spaceshipId);
        }
        return null;
    }

    @Override
    public List<SpaceshipComm> getAllOnlineSpaceships(int requestorId) {
        List<SpaceshipComm> spaceships = new ArrayList<>();

        for (Map.Entry<Integer, SpaceshipComm> spaceship : this.clientSpaceships.entrySet()) {
            //If the same as the requestor space ship ignore
            if (requestorId != spaceship.getKey()) {
                spaceships.add(spaceship.getValue());
            }
        }

        return spaceships;
    }

    @Override
    public void updateSpaceship(SpaceshipComm spaceship) {
        for (Map.Entry<Integer, SpaceshipComm> ship : this.clientSpaceships.entrySet()) {
            if (ship.getKey() == spaceship.getId()) {
                SpaceshipComm updateShip = ship.getValue();
                updateShip.setX(spaceship.getX());
                updateShip.setY(spaceship.getY());
                updateShip.setDirection(spaceship.getDirection());
                updateShip.setResources(spaceship.getResources());
            }
        }
    }
    
    @Override
    public SpaceshipComm getDatabaseSpaceship(String username) throws ClassNotFoundException, SQLException {
        return spaceshipDAO.findDatabaseSpaceship(username);
    }
    
    @Override
    public List<SpaceshipComm> getAllOnlineSpaceships() {
        return (List<SpaceshipComm>) clientSpaceships.values();
    }

    @Override
    public void updateSpaceshipDatabase(SpaceshipComm spaceship) throws ClassNotFoundException, SQLException {
        spaceshipDAO.updateDatabaseSpaceship(spaceship);
    }
        

    /**
     * Send all the SpaceshipComm objects
     */
    public void sendSpaceshipComms(Map<GameClient, Integer> clients) {
        //For every client connected
        for (Map.Entry<GameClient, Integer> entry : clients.entrySet()) {
            try{
            GameClient gameClient = entry.getKey();
            Integer senderShipId = entry.getValue();
            
            PrintWriter writer = new PrintWriter(gameClient.getSocket().getOutputStream(), true);
            
            for(SpaceshipComm ship : this.clientSpaceships.values()){
                if(ship.getId() != senderShipId){
                    String json = Serializer.serializeSpaceShipAsGamePacket(ship.getHeader(), ship.getId(), ship.getX(), ship.getY(), ship.getDirection(), ship.getResources(), ship.isMining());
                    writer.println(json);
                }
            }
            } catch(IOException ex){
                System.err.println(String.format("@@@[SPACESHIP ERROR] IOException in SpaceshipServiceImpl.sendSpaceshipComms - %s", ex.getMessage()));
            }
        }
    }

    /**
     * 
     * @param shipId
     * @param conn 
     */
    public void sendSpaceshipComm(Integer shipId , GameClient conn) {
        try {
            PrintWriter writer = new PrintWriter(conn.getSocket().getOutputStream(), true);
            SpaceshipComm ship = clientSpaceships.get(shipId);
            String json = Serializer.serializeSpaceShipAsGamePacket(ship.getHeader(), ship.getId(), ship.getX(), ship.getY(), ship.getDirection(), ship.getResources(), ship.isMining());
            
            //Send
            writer.println(json);
        } catch (IOException ex) {
            System.err.println(String.format("@@@[SPACESHIP ERROR] IOException in PlayerLocationModule.sendSpaceshipComm() - %s", ex.getMessage()));
        }
    }

    

    

    
}

package com.modules;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.objects.Spaceship;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class PlayerLocationModule {

    private final Gson gson;
    private Map<ClientConnection, Spaceship> clientSpaceships;

    public PlayerLocationModule() {
        gson = new Gson();
        this.clientSpaceships = new HashMap<>();
    }
    
    public void addSpaceship(Spaceship newShip, ClientConnection connection){
        if(!clientSpaceships.containsValue(newShip)){
            System.out.println("added spaceship");
            clientSpaceships.put(connection, newShip);
        }
    }
    
    public Map<ClientConnection, Spaceship> getClientSpaceships() {
        return clientSpaceships;
    }

    private List<Spaceship> getAllPositions(Spaceship requestor) {
        List<Spaceship> positions = new ArrayList<>();

        for (Map.Entry<ClientConnection, Spaceship> sender : this.clientSpaceships.entrySet()) {
            //If the same as the requestor space ship ignore
            //if (requestor != sender.getKey()) {
            positions.add(sender.getValue());
            //}
        }
        
        return positions;
    }

    public void sendPositions() {
        for (Map.Entry<ClientConnection, Spaceship> client : this.clientSpaceships.entrySet()) {
            try {
                if (client.getKey() != null) {
                    //the writer which needs to send a list of positions
                    PrintWriter writer = new PrintWriter(client.getKey().getSocket().getOutputStream(), true);
                    
                    List<Spaceship> positions = getAllPositions(client.getValue());
                    //System.out.println(String.format("Broadcasted positions %s", positions));
                    
                    Type com = new TypeToken<List<Spaceship>>(){}.getType();
                    String json = gson.toJson(positions, com);
                    writer.println(json);
                }
            } catch (IOException ex) {
                Logger.getLogger(PlayerLocationModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void updateSpaceship(int spaceshipID, double x, double y, int direction) {
        //System.out.println(String.format("%s %s %s %s ", new Object[]{spaceshipID,x , y, direction}));
        for (Map.Entry<ClientConnection, Spaceship> client : this.clientSpaceships.entrySet()) {
            if (client.getValue().getId() == spaceshipID) {
                Spaceship updateShip = client.getValue();
                updateShip.update(x, y, direction);
                
            }
        }
    }
    
    public void logout(ClientConnection connection) {
        if(this.clientSpaceships.containsKey(connection)){
            this.clientSpaceships.remove(connection);
        }
    }
}

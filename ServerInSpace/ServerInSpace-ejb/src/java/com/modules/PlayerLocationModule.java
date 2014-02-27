package com.modules;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.objects.Spaceship;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
    private Map<Spaceship, ClientConnection> clientSpaceships;

    public PlayerLocationModule() {
        gson = new Gson();
        this.clientSpaceships = new HashMap<>();
    }
    
    public void addSpaceship(Spaceship newShip, ClientConnection user){
        if(!clientSpaceships.containsKey(newShip)){
            clientSpaceships.put(newShip, user);
        }
    }

    private String getAllPositions(Spaceship requestor) {
        List<String> positions = new ArrayList<>();

        for (Map.Entry<Spaceship, ClientConnection> sender : this.clientSpaceships.entrySet()) {
            //If the same as the requestor space ship ignore
            if (requestor != sender.getKey()) {
                positions.add(gson.toJson(sender.getKey(), Spaceship.class));
            }
        }
        
        String positionString = gson.toJson(positions, List.class);
        return positionString;
    }

    public void sendPositions() {
        for (Map.Entry<Spaceship, ClientConnection> client : this.clientSpaceships.entrySet()) {
            try {
                if (client.getValue() != null) {
                    //the writer which needs to send a list of positions
                    PrintWriter writer = new PrintWriter(client.getValue().getSocket().getOutputStream(), true);

                    String positions = getAllPositions(client.getKey());
                    writer.write(positions);
                }
            } catch (IOException ex) {
                Logger.getLogger(PlayerLocationModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

    public void updateSpaceship(JsonReader reader) {
        try {
            int spaceshipID = -1;
            double x = 0.0f;
            double y = 0.0f;
            int direction = 0;
            
            reader.beginObject();
            while(reader.hasNext()){
                String tagName = reader.nextName();
                switch(tagName){
                    case "id":
                        spaceshipID = reader.nextInt();
                        break;

                    case "x":
                        x = reader.nextDouble();
                        break;

                    case "y":
                        y = reader.nextDouble();
                        break;

                    case "d":
                        direction = reader.nextInt();
                        break;
                        
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            
            for (Map.Entry<Spaceship, ClientConnection> client : this.clientSpaceships.entrySet()) {
                if (client.getKey().getId() == spaceshipID) {
                    Spaceship updateShip = client.getKey();
                    updateShip.update(x, y, direction);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PlayerLocationModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

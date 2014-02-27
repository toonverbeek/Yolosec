package com.modules;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

/**
 *
 * @author user
 */
public class ClientConnection implements Runnable {

    private final Socket socket;
    private InputStream inputStream;
    private final ClientConnectionModule server;
    
    private Gson gson;

    public Socket getSocket() {
        return socket;
    }

    public ClientConnection(Socket socket, ClientConnectionModule server) {
        this.server = server;
        this.socket = socket;
        this.gson = new Gson();
        
        try {
            inputStream = this.socket.getInputStream();
        } catch (IOException ex) {
            try {
                System.out.println("Could not retrieve socket Input or Output stream");
                System.out.println(String.format("Error in PlayerLocationThread constructor %s", ex.getMessage()));
                this.socket.close();
            } catch (IOException ex1) {
                System.out.println("Could not close socket");
                System.out.println(String.format("Error in PlayerLocationThread constructor %s", ex1.getMessage()));
            }
        }
    }

    @Override
    public void run() {
        JsonReader reader = new JsonReader(new InputStreamReader(this.inputStream));
        reader.setLenient(true);
        while (socket.isConnected()) {
            try {
                if (reader.hasNext()) {
                    this.readGson(reader);
                }
            } catch (IOException ex) {
                //System.out.println("Connection lost");
            }
        }
        server.logout(this);
    }

    private void readGson(JsonReader reader) {
            Map recievedObject = gson.fromJson(reader, Map.class);
            //System.out.println(recievedObject.toString());
            
            String header = (String) recievedObject.get("header");
            
            switch (header) {
            case "SpaceshipComm":
                int spaceshipID = ((Double)recievedObject.get("id")).intValue();
                double x = (double) recievedObject.get("x");
                double y = (double) recievedObject.get("y");
                int d = ((Double)recievedObject.get("d")).intValue();
                this.server.updateSpaceship(spaceshipID, x, y, d);
                break;

            case "LoginComm":
                String username = (String) recievedObject.get("username");
                String password = (String) recievedObject.get("password");
                this.server.login(username, password, this);
                break;

        }
    }
}

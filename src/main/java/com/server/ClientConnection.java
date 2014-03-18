package com.server;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.GamePacket;
import com.ptsesd.groepb.shared.LoginComm;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.SpaceshipComm;

/**
 *
 * @author user
 */
public class ClientConnection implements Runnable {

    private final Socket socket;
    private InputStream inputStream;
    private final ConnectionServer server;
    
    private final Gson gson;

    public Socket getSocket() {
        return socket;
    }

    public ClientConnection(Socket socket, ConnectionServer server) {
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
        while (!socket.isClosed()) {
            try {
                if (reader.hasNext()) {
                    this.readGson(reader);
                }
            } catch (IOException ex) {
                  System.out.println("Connection lost");
                  break;
            }
        }
        server.logout(this);
    }

    private void readGson(JsonReader reader) {
        try {
            List<GamePacket> deserializePackets = Serializer.getGamePackets(reader);
            
            for (GamePacket packet : deserializePackets) {
                String header = packet.getHeader();
            switch (header) {
                    case "SpaceshipComm":
                        SpaceshipComm comm = (SpaceshipComm)packet;
                        this.server.updateSpaceship(comm);
                        break;

                    case "LoginComm":
                        LoginComm lcomm = (LoginComm)packet;
                        this.server.login(lcomm, this);
                        break;
                        
                    case "AsteroidComm":
                        AsteroidComm acomm = (AsteroidComm)packet;
                        this.server.recievedAsteroid(this, acomm);
                        break;

                    default:
                        System.out.println(String.format("Recieved Unknown Package - %s", packet.toString()));

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

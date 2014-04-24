package com.yolosec.domain;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.MessagingComm;
import com.ptsesd.groepb.shared.GamePacket;
import com.ptsesd.groepb.shared.socket.InventoryReply;
import com.ptsesd.groepb.shared.socket.InventoryRequest;
import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.LoginComm;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.SpaceshipComm;
import com.yolosec.service.GameService;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class GameClient implements Runnable {
    
    private final Socket socket;
    private InputStream inputStream;
    private final GameService server;
    
    private final Gson gson;
    
    public Socket getSocket() {
        return socket;
    }
    
    public GameClient(Socket socket, GameService server) {
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
                System.out.println("---[CONNECTION] Connection lost");
                break;
            }
        }
        server.updateSpaceshipDatabase(this);
        server.logout(this);
    }
    
    private void readGson(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.BEGIN_ARRAY) {
            List<GamePacket> packets = Serializer.deserializePackets(reader);
            for (GamePacket packet : packets) {
                readPacket(packet);
            }
        } else {
            GamePacket packet = Serializer.getSingleGamePacket(reader);
            readPacket(packet);
        }
    }
    
    private void readPacket(GamePacket packet) {
        String header = packet.getHeader();
        
        switch (header) {
            case "SpaceshipComm":
                SpaceshipComm comm = (SpaceshipComm) packet;
                this.server.updateSpaceship(comm);
                break;
            
            case "LoginComm":
                LoginComm lcomm = (LoginComm) packet;
                this.server.login(lcomm, this);
                break;
            
            case "AsteroidComm":
                AsteroidComm acomm = (AsteroidComm) packet;
                this.server.recievedAsteroid(acomm);
                this.server.broadcastAsteroids();
                //System.out.println("Aster" + acomm);
                break;
            
            case "MessageComm":
                MessagingComm mcomm = (MessagingComm) packet;
                this.server.receivedMessage(mcomm);
                this.server.broadcastMessages();
                break;
                
            case "InventoryRequest":
                InventoryRequest invReq = (InventoryRequest) packet;
                sendInventoryReply(invReq.getSpaceshipId());
                break;
                
            
            default:
                System.out.println(String.format("---[CONNECTION] Recieved Unknown Package - %s", packet.toString()));
        }
    }
    
    private void sendInventoryReply(int spaceshipId) {
        PrintWriter writer = null;
        try {
            //Get the inventory from the economy system
            List<ItemComm> inventory = this.server.getInventory(spaceshipId);
            
            //Create a return packet
            InventoryReply reply = new InventoryReply(InventoryReply.class.getSimpleName(), spaceshipId, inventory);
            
            //Convert packet to json
            String json = Serializer.serializeInventoryReplyAsGamePacktet(reply);
            
            //Write output to client
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(json);
        } catch (IOException ex) {
            System.err.println(String.format("---[CONNECTION] Could not send inventory reply - %s ", ex.getMessage()));
        } 
    }
}

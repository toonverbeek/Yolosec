package com.modules;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author user
 */
public class ClientConnection implements Runnable {

    private final Socket socket;
    private InputStream inputStream;
    private final ClientConnectionModule server;

    public Socket getSocket() {
        return socket;
    }

    public ClientConnection(Socket socket, ClientConnectionModule server) {
        this.server = server;
        this.socket = socket;
        
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
    }

    private void readGson(JsonReader reader) {
       try {
            reader.beginObject();

            while (reader.hasNext()) {
                String nextName = reader.nextName();

                switch (nextName) {
                    
                    case "SpaceshipComm":
                        this.server.updateSpaceship(reader);
                        break;
                    
                    case "Account":
                        this.server.login(reader, this);
                        break;
                        
                    default:
                        reader.skipValue();
                        break;

                }
            }
            reader.endObject();
        } catch (IOException ex) {
            System.out.println("Could not readGson");
            System.out.println(String.format("Error in PlayerLocationThread.readGson() : %s", ex.getMessage()));
        }
    }
}

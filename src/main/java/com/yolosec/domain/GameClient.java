package com.yolosec.domain;

import com.yolosec.service.GameService;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.GamePacket;
import com.ptsesd.groepb.shared.LoginComm;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.SpaceshipComm;
import com.yolosec.service.GameService;

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

                    default:
                        System.out.println(String.format("---[CONNECTION] Recieved Unknown Package - %s", packet.toString()));
                }
            }
        } else {
            GamePacket packet = Serializer.getSingleGamePacket(reader);
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

                default:
                    System.out.println(String.format("---[CONNECTION] Recieved Unknown Package - %s", packet.toString()));
            }
        }
    }
}

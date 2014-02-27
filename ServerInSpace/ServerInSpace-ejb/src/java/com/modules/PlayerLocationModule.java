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

    private ServerSocket server;
    private final Gson gson;

    private boolean isRunning = false;

    private Spaceship testShip;
    private Spaceship testShip2;
    private Map<Spaceship, PlayerLocationThread> clients;

    public PlayerLocationModule() {
        gson = new Gson();
        this.clients = new HashMap<>();
        try {
            testShip = new Spaceship(1, 0.0f, 0.0f, 0);
            testShip2 = new Spaceship(2, 0.0f, 0.0f, 0);
            clients.put(testShip, null);
            clients.put(testShip2, null);
            this.server = new ServerSocket(1337);

            isRunning = true;
            this.run();
        } catch (IOException ex) {
            System.err.println("Could not listen on port 912");
            System.exit(-1);
        }
    }

    public void run() {
        System.out.println("Started PlayerLocationModule run()");
        while (isRunning) {
            try {
                Socket newClient = server.accept();
                newClient.setKeepAlive(true);

                PlayerLocationThread runnable = new PlayerLocationThread(newClient, this);

                if (clients.get(0) == null) {
                    clients.put(testShip, runnable);
                } else if (clients.get(1) == null) {
                    clients.put(testShip2, runnable);
                }

                Thread t = new Thread(runnable);
                t.start();

                //TODO: getSpaceship
                //classes.getSpaceshipBySocket(newClient);
                System.out.println(String.format("New socket request %s", newClient.getInetAddress().getHostAddress()));
                sendPositions();
            } catch (IOException ex) {
                System.out.println("Could not connect with newClient");
            }
        }
    }

    private List getAllPositions(Spaceship requestor) {
        List<String> positions = new ArrayList<>();

        for (Map.Entry<Spaceship, PlayerLocationThread> sender : this.clients.entrySet()) {
            //If the same as the requestor space ship ignore
            if (requestor != sender.getKey()) {
                positions.add(gson.toJson(sender.getKey(), Spaceship.class));
            }
        }

        return positions;
    }

    private void sendPositions() {
        for (Map.Entry<Spaceship, PlayerLocationThread> client : this.clients.entrySet()) {
            try {
                if (client.getValue() != null) {
                    //the writer which needs to send a list of positions
                    PrintWriter writer = new PrintWriter(client.getValue().getSocket().getOutputStream(), true);

                    List<String> positions = getAllPositions(client.getKey());

                    for (String pos : positions) {
                        writer.write(pos);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(PlayerLocationModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void updateSpaceship(int id, double x, double y, int direction) {
        for (Map.Entry<Spaceship, PlayerLocationThread> client : this.clients.entrySet()) {
            if (client.getKey().getId() == id) {
                PrintWriter writer = null;
                try {
                    Spaceship updateShip = client.getKey();
                    updateShip.update((float) x, (float) y, direction);
                    
                    writer = new PrintWriter(client.getValue().getSocket().getOutputStream(), true);
                    writer.print("");
                } catch (IOException ex) {
                    Logger.getLogger(PlayerLocationModule.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    writer.close();
                }
            }
        }
    }

    private class PlayerLocationThread implements Runnable {

        private final Socket socket;
        private InputStream inputStream;
        private Gson gson;
        private PlayerLocationModule module;

        public Socket getSocket() {
            return socket;
        }

        public PlayerLocationThread(Socket socket, PlayerLocationModule module) {
            this.module = module;

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
        }

        private void readGson(JsonReader reader) {
            int spaceshipID = -1;
            double x = 0;
            double y = 0;
            int direction = 0;

            try {
                reader.beginObject();

                while (reader.hasNext()) {
                    String nextName = reader.nextName();

                    switch (nextName) {
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

                System.out.println(String.format("ID %s X %s Y %s D %s", new Object[]{spaceshipID, x, y, direction}));
                if (spaceshipID != -1) {
                    this.module.updateSpaceship(spaceshipID, x, y, direction);
                }

            } catch (IOException ex) {
                System.out.println("Could not readGson");
                System.out.println(String.format("Error in PlayerLocationThread.readGson() : %s", ex.getMessage()));
            }
        }
    }
}

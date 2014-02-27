package com.modules;

import com.google.gson.stream.JsonReader;
import com.objects.Spaceship;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author user
 */
public class ClientConnectionModule {

    private ServerSocket server;

    private boolean isRunning = false;

    //Module responsible for tracking the locations of spaceship
    private PlayerLocationModule locationModule;

    //Module responsible for keeping track of the logged in users
    private PlayerLoginModule loginModule;

    public ClientConnectionModule() {
        locationModule = new PlayerLocationModule();
        loginModule = new PlayerLoginModule(this);

        try {
            this.server = new ServerSocket(1337);

            isRunning = true;
        } catch (IOException ex) {
            System.err.println("Could not listen on port 912");
            System.err.println(String.format("Exception in ClientConnectionModule constructor : %s ", ex.getMessage()));
            System.exit(-1);
        }
    }

    public void run() {
        System.out.println("Started PlayerLocationModule run()");
        while (isRunning) {
            try {
                Socket newClient = server.accept();
                newClient.setKeepAlive(true);

                ClientConnection runnable = new ClientConnection(newClient, this);

                Thread t = new Thread(runnable);
                t.start();

                System.out.println(String.format("New socket request %s", newClient.getInetAddress().getHostAddress()));
            } catch (IOException ex) {
                System.out.println("Could not connect with newClient");
            }
        }
    }

    public synchronized boolean login(JsonReader reader, ClientConnection connection) {
        return loginModule.login(reader, connection);
    }

    public synchronized void updateSpaceship(JsonReader reader) {
        locationModule.updateSpaceship(reader);
    }

    public void addSpaceship(Spaceship spaceship, ClientConnection connection) {
        locationModule.addSpaceship(spaceship, connection);
    }

}

package com.modules;

import com.objects.Spaceship;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author user
 */
public class ClientConnectionModule implements Runnable {

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
            System.err.println("Could not listen on port 1337");
            System.err.println(String.format("Exception in ClientConnectionModule constructor : %s ", ex.getMessage()));
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        System.out.println("Started PlayerLocationModule run()");
        while (isRunning) {
            try {
                Socket newClient = server.accept();
                newClient.setKeepAlive(true);

                ClientConnection runnable = new ClientConnection(newClient, this);

                Thread t = new Thread(runnable);
                t.start();
                System.out.println("\n");
                System.out.println(String.format("New socket request %s", newClient.getInetAddress().getHostAddress()));
            } catch (IOException ex) {
                System.out.println("Could not connect with newClient");
            }
        }
    }
    
    public synchronized void logout(ClientConnection connection){
        loginModule.logout(connection);
        locationModule.logout(connection);
    }

    public synchronized boolean login(String username, String password, ClientConnection connection) {
        return loginModule.login(username, password, connection);
    }

    public synchronized void updateSpaceship(int id, double x, double y, int direction) {
        locationModule.updateSpaceship(id, x, y, direction);
    }

    public void addSpaceship(Spaceship spaceship, ClientConnection connection) {
        locationModule.addSpaceship(spaceship, connection);
    }
    
    public void broadcastPositions(){
        locationModule.sendPositions();
    }
    
    public String getStatusInformation(){
        Map<ClientConnection, Spaceship> clientSpaceships = locationModule.getClientSpaceships();
        StringBuilder builder = new StringBuilder();
        
        builder.append(String.format("Status information { %s } \n", new Date().toString()));
        
        for(Map.Entry<ClientConnection, Spaceship> connection : clientSpaceships.entrySet()){
            Spaceship key = connection.getValue();
            builder.append(String.format("Spaceship ID {%s} X {%s} Y {%s} DIRECTION {%s}", new Object[]{key.getId(), key.getX(), key.getY(), key.getDirection()}));
            
            ClientConnection value = connection.getKey();
            builder.append(String.format("Connection IP {%s} PORT {%s} \n" , new Object[]{value.getSocket().getLocalAddress().toString(), value.getSocket().getPort()}));
        }
        
        return builder.toString();
    }

}

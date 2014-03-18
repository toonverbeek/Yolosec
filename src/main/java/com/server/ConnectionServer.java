package com.server;

import com.modules.player.ClientBroadcastModule;
import com.modules.player.PlayerLocationModule;
import com.modules.player.PlayerLoginModule;
import com.modules.player.PlayerResourceModule;
import com.modules.server.ServerAsteroidGenerator;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.LoginComm;
import com.ptsesd.groepb.shared.SpaceshipComm;

/**
 *
 * @author user
 */
public class ConnectionServer implements Runnable {

    private ServerSocket server;

    private boolean isRunning = false;

    //Module responsible for tracking the locations of spaceship
    private PlayerLocationModule locationModule;

    //Module responsible for keeping track of the logged in users
    private PlayerLoginModule loginModule;
    
    //Module responsible for updating the resources of a user
    private PlayerResourceModule resourceModule;
    
    private ClientBroadcastModule broadcastModule;
    
    private ServerAsteroidGenerator asteroidModule;

    public ConnectionServer(ClientBroadcastModule broadcastModule) {
        this.broadcastModule = broadcastModule;
        locationModule = new PlayerLocationModule();
        resourceModule = new PlayerResourceModule();
        loginModule = new PlayerLoginModule(this);
        asteroidModule = new ServerAsteroidGenerator();

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

    public synchronized boolean login(LoginComm lcomm, ClientConnection connection) {
        return loginModule.login(lcomm, connection);
    }

    public synchronized void updateSpaceship(SpaceshipComm ship) {
        locationModule.updateSpaceship(ship);
    }

    public void addSpaceship(SpaceshipComm spaceship, ClientConnection connection) {
        locationModule.addSpaceship(spaceship, connection);
    }
    
    public synchronized void recievedAsteroid(ClientConnection connection, AsteroidComm asteroid){
        int clientID = locationModule.getClientSpaceship(connection).getId();
        resourceModule.recievedAsteroid(asteroid, clientID);
    }
    
    public void broadcastPositions(){
        locationModule.sendPositions();
    }
    
    public String getStatusInformation(){
        Map<ClientConnection, SpaceshipComm> clientSpaceships = locationModule.getClientSpaceships();
        StringBuilder builder = new StringBuilder();
        
        builder.append(String.format("\n Status information { %s } \n", new Date().toString()));
        
        for(Map.Entry<ClientConnection, SpaceshipComm> connection : clientSpaceships.entrySet()){
            SpaceshipComm key = connection.getValue();
            builder.append(String.format("Spaceship ID {%s} X {%s} Y {%s} DIRECTION {%s}", new Object[]{key.getId(), key.getX(), key.getY(), key.getDirection()}));
            
            ClientConnection value = connection.getKey();
            builder.append(String.format("Connection IP {%s} PORT {%s} \n" , new Object[]{value.getSocket().getLocalAddress().toString(), value.getSocket().getPort()}));
            builder.append("-----------------------------------------------------------");
        }
        
        return builder.toString();
    }
    
    public void broadcastAsteroids(){
        broadcastModule.broadcastAsteroids();
    }

    public List<AsteroidComm> getAsteroids() {
        return asteroidModule.getAsteroids();
    }

    public List<ClientConnection> getClientConnections() {
        return locationModule.getClientConnections();
    }

}

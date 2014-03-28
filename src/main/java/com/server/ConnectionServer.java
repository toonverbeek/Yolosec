package com.server;

import com.modules.thread.ClientBroadcastThread;
import com.modules.player.SpaceshipCommModule;
import com.modules.player.PlayerLoginModule;
import com.modules.player.AsteroidCommModule;
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
    public static boolean debug = true;
    private ServerSocket server;

    private boolean isRunning = false;

    //Module responsible for tracking the locations of spaceship
    private SpaceshipCommModule spaceshipCommModule;

    //Module responsible for keeping track of the logged in users
    private PlayerLoginModule loginModule;
    
    //Module responsible for updating the resources of a user
    private AsteroidCommModule resourceModule;
    
    private ClientBroadcastThread broadcastModule;
    
    private ServerAsteroidGenerator asteroidModule;

    public ConnectionServer(ClientBroadcastThread broadcastModule) {
        this.broadcastModule = broadcastModule;
        spaceshipCommModule = new SpaceshipCommModule(this);
        loginModule = new PlayerLoginModule(this);
        asteroidModule = new ServerAsteroidGenerator();
        resourceModule = new AsteroidCommModule(asteroidModule.generateAsteroids());
        
        try {
            this.server = new ServerSocket(1337);

            isRunning = true;
        } catch (IOException ex) {
            System.err.println("---[SERVER] Could not listen on port 1337");
            System.err.println(String.format("---[SERVER] Exception in ClientConnectionModule constructor : %s ", ex.getMessage()));
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        System.out.println("---[SERVER] Started PlayerLocationModule run()");
        while (isRunning) {
            try {
                Socket newClient = server.accept();
                newClient.setKeepAlive(true);
                
                ClientConnection runnable = new ClientConnection(newClient, this);
                
                Thread t = new Thread(runnable);
                t.start();
                System.out.println("\n");
                System.out.println(String.format("---[SERVER] New socket request %s", newClient.getInetAddress().getHostAddress()));
            } catch (IOException ex) {
                System.out.println("---[SERVER] Could not connect with newClient");
            }
        }
    }
    
    public List<AsteroidComm> getAsteroids() {
        return resourceModule.getAsteroids();
    }

    public List<ClientConnection> getClientConnections() {
        return spaceshipCommModule.getClientConnections();
    }
    
    public synchronized void logout(ClientConnection connection){
        loginModule.logout(connection);
        spaceshipCommModule.removeSpaceshipComm(connection);
    }

    public synchronized boolean login(LoginComm lcomm, ClientConnection connection) {
        return loginModule.login(lcomm, connection);
    }

    public synchronized void updateSpaceship(SpaceshipComm ship) {
        spaceshipCommModule.updateSpaceshipComm(ship);
    }

    public void addSpaceship(SpaceshipComm spaceship, ClientConnection connection) {
        spaceshipCommModule.addSpaceshipComm(spaceship, connection);
    }
    
    public synchronized void recievedAsteroid(ClientConnection connection, AsteroidComm asteroid){
        int clientID = spaceshipCommModule.getSpaceshipFromClient(connection).getId();
        resourceModule.recievedAsteroid(asteroid);
    }
    
    public void broadcastPositions(){
        spaceshipCommModule.sendSpaceshipComms();
    }
    
    public String getStatusInformation(){
        Map<ClientConnection, SpaceshipComm> clientSpaceships = spaceshipCommModule.getClientSpaceships();
        StringBuilder builder = new StringBuilder();
        
        builder.append(String.format("\n ---[INFO] Status information { %s } \n", new Date().toString()));
        
        for(Map.Entry<ClientConnection, SpaceshipComm> connection : clientSpaceships.entrySet()){
            SpaceshipComm key = connection.getValue();
            builder.append(String.format("---[INFO] Spaceship ID {%s} X {%s} Y {%s} DIRECTION {%s}", new Object[]{key.getId(), key.getX(), key.getY(), key.getDirection()}));
            
            ClientConnection value = connection.getKey();
            builder.append(String.format("---[INFO] Connection IP {%s} PORT {%s} \n" , new Object[]{value.getSocket().getLocalAddress().toString(), value.getSocket().getPort()}));
            
        }
        builder.append("-----------------------------------------------------------");
        
        return builder.toString();
    }
    
    public Boolean logCpuTime() {
        return this.broadcastModule.logCpuTime();
    }
    
    
    public void broadcastAsteroids(){
        spaceshipCommModule.sendAsteroidComms();
    }

    void updateSpaceshipToDatabase(ClientConnection aThis) {
        SpaceshipComm ship = spaceshipCommModule.getSpaceshipFromClient(aThis);
        int[] resources = ship.getResources();
        DbConnector.updateSpaceship(ship.getId(), (int)ship.getX(), (int)ship.getY(), ship.getDirection(), resources[0], resources[1], resources[2]);
    }

    public void resetAsteroids() {
        this.resourceModule.setAsteroids(this.asteroidModule.generateAsteroids());
        this.broadcastAsteroids();
    }
}

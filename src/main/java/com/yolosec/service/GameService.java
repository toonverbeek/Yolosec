package com.yolosec.service;

import com.yolosec.domain.GameClient;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.LoginComm;
import com.ptsesd.groepb.shared.LoginCommError;
import com.ptsesd.groepb.shared.MessagingComm;
import com.ptsesd.groepb.shared.PlanetComm;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.SpaceshipComm;
import com.yolosec.data.AsteroidDAOImpl;
import com.yolosec.data.DatabaseDAO;
import com.yolosec.data.ItemDAO;
import com.yolosec.data.ItemDAOImpl;
import com.yolosec.data.MessageDAOImpl;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class GameService implements Runnable {

    public static boolean debug = true;
    private ServerSocket server;
    private static final int mapSizeX = 16000;
    private static final int mapSizeY = 16000;

    private boolean isRunning = false;

    private Map<GameClient, Integer> clients;

    //Service responsible for the spaceships
    private SpaceshipServiceImpl spaceshipDAO;

    private GameBroadcastService broadcastModule;

    private AsteroidDAOImpl asteroidDAO;
    private MessageDAOImpl messagingDAO;
    private ItemDAO itemDAO;

    public GameService(GameBroadcastService broadcastModule) {
        this.clients = new HashMap<>();

        this.broadcastModule = broadcastModule;
        try {
            spaceshipDAO = new SpaceshipServiceImpl(this);
        } catch (SQLException ex) {
            Logger.getLogger(GameService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameService.class.getName()).log(Level.SEVERE, null, ex);
        }

        asteroidDAO = new AsteroidDAOImpl(mapSizeX, mapSizeY);
        messagingDAO = new MessageDAOImpl();
        itemDAO = new ItemDAOImpl();

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

                GameClient runnable = new GameClient(newClient, this);

                Thread t = new Thread(runnable);
                t.start();
                System.out.println("\n");
                System.out.println(String.format("---[SERVER] New socket request %s", newClient.getInetAddress().getHostAddress()));
            } catch (IOException ex) {
                System.out.println("---[SERVER] Could not connect with newClient");
            }
        }
    }

    /* ---------------------------------------------------------------------------------------------------------------------------
     *  -------------------------------------------------------- Login Methods ----------------------------------------------------
     *  --------------------------------------------------------------------------------------------------------------------------- */
    public synchronized void logout(GameClient connection) {
        System.out.println(String.format("---[LOGOUT] Cleaning up after: %s", connection.getSocket().getInetAddress().getHostAddress()));
        //Remove spaceship information
        int spaceshipId = clients.get(connection);
        spaceshipDAO.removeOnlineSpaceship(spaceshipId);

        //Remove GameClient
        clients.remove(connection);
    }

    public synchronized SpaceshipComm login(LoginComm lcomm, GameClient connection) {
        SpaceshipComm spaceship = null;
        try {
            String checkPassword = DatabaseDAO.identifyUser(lcomm.getUsername());

            if (checkPassword != null && checkPassword.equals((lcomm.getPassword()))) {

                //Get Spaceship from user
                spaceship = spaceshipDAO.getDatabaseSpaceship(lcomm.getUsername());

                //Add the spaceship to the location module
                this.clients.put(connection, spaceship.getId());
                this.spaceshipDAO.addOnlineSpaceship(spaceship);
            }

            System.out.println(String.format("---[LOGIN] Returned logged user = %s || connected = %s", lcomm.getUsername(), spaceship != null));

        } catch (Exception ex) {
            System.out.println(String.format("---[LOGIN] Excepting in PlayerLoginModule.login() - %s", ex.getMessage()));
        }
        sendLoggedIn(spaceship, connection);
        //Always return the login request
        return spaceship;
    }

    public synchronized void sendLoggedIn(SpaceshipComm ship, GameClient conn) {
        if (ship != null) {
            this.spaceshipDAO.sendSpaceshipComm(ship.getId(), conn);
            this.broadcastAsteroids();
        } else {
            System.out.println("---[LOGIN] SENDING ERROR INLOG");
            this.spaceshipDAO.sendSpaceshipComm(-1, conn);
        }
    }

    public synchronized void sendLoginError(GameClient conn) {
        try {
            PrintWriter writer = new PrintWriter(conn.getSocket().getOutputStream(), true);
            LoginCommError er = new LoginCommError(LoginCommError.class.getSimpleName());
            String json = Serializer.serializeLoginCommErrorAsGamePacktet(er);
            //Send
            writer.println(json);
        } catch (IOException ex) {
            System.err.println(String.format("@@@[LOGIN] Excepting in PlayerLoginModule.sendLoginError() - %s", ex.getMessage()));
        }
    }

    /* ----------------------------------------------------------------------------------------------------------------------------
     *  ---------------------------------------------------- Asteroid Methods -----------------------------------------------------
     *  --------------------------------------------------------------------------------------------------------------------------- */
    public List<AsteroidComm> getAsteroids() {
        return asteroidDAO.findAll();
    }

    public synchronized void recievedAsteroid(AsteroidComm asteroid) {
        this.asteroidDAO.updateAsteroid(asteroid);
    }

    public void resetAsteroids() {
        this.asteroidDAO.resetAsteroids();
    }

    public void broadcastAsteroids() {
        List<GameClient> clien = new ArrayList<>(this.clients.keySet());
        asteroidDAO.sendAsteroidComms(clien);
    }
    
    /* ----------------------------------------------------------------------------------------------------------------------------
     *  --------------------------------------------------- Spaceship Methods -----------------------------------------------------
     *  --------------------------------------------------------------------------------------------------------------------------- */
    public synchronized void updateSpaceship(SpaceshipComm ship) {
        spaceshipDAO.updateSpaceship(ship);
    }

    public synchronized void updateSpaceshipDatabase(GameClient conn) {
        try {
            int shipId = clients.get(conn);
            System.out.println("---[UPDATESHIP] ship ID: " + shipId);
            SpaceshipComm onlineSpaceship = spaceshipDAO.getOnlineSpaceship(shipId);
            if (onlineSpaceship != null) {
                spaceshipDAO.updateSpaceshipDatabase(onlineSpaceship);
            }
        } catch (ClassNotFoundException ex) {
            System.err.println(String.format("@@@[ERROR] MySQL connector not found in GameService.updateSpaceshipDatabase() - %s", ex.getMessage()));
        } catch (SQLException ex) {
            System.err.println(String.format("@@@[ERROR] SQL Exception in GameService.updateSpaceshipDatabase() - %s", ex.getMessage()));
        } catch (Exception ex) {
            System.err.println(String.format("@@@[ERROR] Exception in GameService.updateSpaceshipDatabase() - %s", ex.getMessage()));
        }

    }

    public void broadcastPositions() {
        spaceshipDAO.sendSpaceshipComms(clients);
    }
    
    /* ----------------------------------------------------------------------------------------------------------------------------
     *  ------------------------------------------------- Item Methods ------------------------------------------------------------
     *  --------------------------------------------------------------------------------------------------------------------------- */
    
    public List<ItemComm> getInventory(int spaceshipId){
        return itemDAO.getInventory(spaceshipId);
    }
    
    public List<ItemComm> getAuctionHouse(){
        return itemDAO.getAuctionHouse();
    }
    
    public void buyItem(int spaceshipId, long itemId){
        itemDAO.buyItem(spaceshipId, itemId);
    }
    
    public void sellItem(int spaceshipId, long itemId){
        itemDAO.sellItem(spaceshipId, itemId);
    }
    
    public void cancelAuction(int spaceshipId, long itemId){
        itemDAO.cancelAuction(spaceshipId, itemId);
    }

    /* ---------------------------------------------------------------------------------------------------------------------------
     *  ------------------------------------------------- Messaging Methods -------------------------------------------------------
     *  --------------------------------------------------------------------------------------------------------------------------- */
    public synchronized void receivedMessage(MessagingComm message) {
        try {
            message.setTimestamp();
            this.messagingDAO.addMessage(message);
        } catch (Exception ex) {
            System.err.println(String.format("@@@[ERROR] Exception in GameService.updateMessageDatabase() - %s", ex.getMessage()));
        }
    }

    public void broadcastMessages() {
        List<GameClient> cliens = new ArrayList<>(this.clients.keySet());
        messagingDAO.sendMessageComms(cliens);
    }

    public List<MessagingComm> getMessages() {
        return messagingDAO.findAll();
    }
    
    public void resetMessages() {
        messagingDAO.resetMessages();
    }
    
    public void getMessagesFromServer() {
        try {
            messagingDAO.getMessages();
        } catch (Exception ex) {
            System.err.println(String.format("@@@[ERROR] Exception in GameService.getMessageDatabase() - %s", ex.getMessage()));
        }
    }
    
    /* ---------------------------------------------------------------------------------------------------------------------------
     *  ------------------------------------------------- Information Methods -----------------------------------------------------
     *  --------------------------------------------------------------------------------------------------------------------------- */
    public String getStatusInformation() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("\n ---[INFO] Status information { %s } \n", new Date().toString()));

        for (Map.Entry<GameClient, Integer> connection : clients.entrySet()) {
            SpaceshipComm ship = spaceshipDAO.getOnlineSpaceship(connection.getValue());
            builder.append(String.format("---[INFO] Spaceship ID {%s} X {%s} Y {%s} DIRECTION {%s}", new Object[]{ship.getId(), ship.getX(), ship.getY(), ship.getDirection()}));

            GameClient value = connection.getKey();
            builder.append(String.format("---[INFO] Connection IP {%s} PORT {%s} \n", new Object[]{value.getSocket().getLocalAddress().toString(), value.getSocket().getPort()}));

        }
        builder.append("-----------------------------------------------------------");

        return builder.toString();
    }

    public Boolean logCpuTime() {
        return this.broadcastModule.logCpuTime();
    }
}

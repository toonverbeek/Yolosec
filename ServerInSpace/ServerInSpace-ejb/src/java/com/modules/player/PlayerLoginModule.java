package com.modules.player;


import com.server.ConnectionServer;
import com.server.ClientConnection;
import com.objects.User;
import com.server.DbConnector;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.SpaceshipComm;

/**
 *
 * @author user
 */
public class PlayerLoginModule {

    private Map<ClientConnection, User> clientConnections;

    private final ConnectionServer server;

    public PlayerLoginModule(ConnectionServer clientConnectionModule) {
        clientConnections = new HashMap<>();
        server = clientConnectionModule;
    }

    public synchronized boolean login(String username, String password, ClientConnection connection) {
        System.out.println("Login request");
        boolean isLoggedIn = false;
        try {
            String checkPassword = DbConnector.identifyUser(username);

            if (checkPassword != null && checkPassword.equals(password)) {

                //Get Spaceship from user
                SpaceshipComm spaceship = DbConnector.getSpaceship(username);

                //Add the spaceship to the location module
                this.server.addSpaceship(spaceship, connection);

                //Set logged in true
                isLoggedIn = true;
            }

            System.out.println(String.format("Returned logged in is [%s]", isLoggedIn));
            //Always return the login request
        } catch (Exception ex) {
            Logger.getLogger(PlayerLoginModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isLoggedIn;
    }

    public void logout(ClientConnection connection) {
        if(this.clientConnections.containsKey(connection)){
            this.clientConnections.remove(connection);
        }
    }
}

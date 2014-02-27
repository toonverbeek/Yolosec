package com.modules;

import com.objects.Spaceship;
import com.objects.User;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author user
 */
public class PlayerLoginModule {

    private Map<ClientConnection, User> clientConnections;

    private final ClientConnectionModule server;

    public PlayerLoginModule(ClientConnectionModule clientConnectionModule) {

        clientConnections = new HashMap<>();

        server = clientConnectionModule;
    }

    public synchronized boolean login(String username, String password, ClientConnection connection) {
        System.out.println("Login request");
        boolean isLoggedIn = false;

        System.out.println(password);
        //String checkPassword = DbConnector.identifyUser(username);
        String checkPassword = "asdf";

        if (checkPassword != null && checkPassword.equals(password)) {
               //Get Spaceship from user
            //Spaceship spaceship = DbConnector.getSpaceship(username);
            Spaceship spaceship = new Spaceship(2, 0.0f, 0.0f, 0);

            //Add the spaceship to the location module
            this.server.addSpaceship(spaceship, connection);

            //Set logged in true
            isLoggedIn = true;
        }
        
        System.out.println(String.format("Returned loggin is %s ", isLoggedIn));
        //Always return the login request
        return isLoggedIn;
    }

    public void logout(ClientConnection connection) {
        if(this.clientConnections.containsKey(connection)){
            this.clientConnections.remove(connection);
        }
    }
}

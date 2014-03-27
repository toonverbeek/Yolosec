package com.modules.player;


import com.server.ConnectionServer;
import com.server.ClientConnection;
import com.objects.User;
import com.server.DbConnector;
import java.util.HashMap;
import java.util.Map;
import com.ptsesd.groepb.shared.LoginComm;
import com.ptsesd.groepb.shared.SpaceshipComm;

/**
 *
 * @author user
 */
public class PlayerLoginModule {

    private Map<ClientConnection, User> clientConnections;

    public Map<ClientConnection, User> getClientConnections() {
        return clientConnections;
    }

    private final ConnectionServer server;

    public PlayerLoginModule(ConnectionServer clientConnectionModule) {
        clientConnections = new HashMap<>();
        server = clientConnectionModule;
    }

    public synchronized boolean login(LoginComm lcomm, ClientConnection connection) {
        System.out.println("---[LOGIN] Login request");
        boolean isLoggedIn = false;
        try {
            String checkPassword = DbConnector.identifyUser(lcomm.getUsername());

            if (checkPassword != null && checkPassword.equals((lcomm.getPassword()))) {

                //Get Spaceship from user
                SpaceshipComm spaceship = DbConnector.getSpaceship((lcomm.getUsername()));

                //Add the spaceship to the location module
                this.server.addSpaceship(spaceship, connection);

                isLoggedIn = true;
            }

            System.out.println(String.format("---[LOGIN] Returned logged user = %s || connected = %s", lcomm.getUsername(), isLoggedIn));
            //Always return the login request
        } catch (Exception ex) {
            System.out.println(String.format("---[LOGIN] Excepting in PlayerLoginModule.login() - %s", ex.getMessage()));
        }
        return isLoggedIn;
    }

    public void logout(ClientConnection connection) {
        if(this.clientConnections.containsKey(connection)){
            this.clientConnections.remove(connection);
        }
    }
}

package com.modules.player;

import com.server.ConnectionServer;
import com.server.ClientConnection;
import com.objects.User;
import com.server.DbConnector;
import java.util.HashMap;
import java.util.Map;
import com.ptsesd.groepb.shared.LoginComm;
import com.ptsesd.groepb.shared.LoginCommError;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.SpaceshipComm;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public synchronized SpaceshipComm login(LoginComm lcomm, ClientConnection connection) {
        System.out.println("---[LOGIN] Login request");
        SpaceshipComm spaceship = null;
        try {
            String checkPassword = DbConnector.identifyUser(lcomm.getUsername());

            if (checkPassword != null && checkPassword.equals((lcomm.getPassword()))) {

                //Get Spaceship from user
                spaceship = DbConnector.getSpaceship((lcomm.getUsername()));

                //Add the spaceship to the location module
                this.server.addSpaceship(spaceship, connection);
            }

            System.out.println(String.format("---[LOGIN] Returned logged user = %s || connected = %s", lcomm.getUsername(), spaceship != null));
            //Always return the login request
        } catch (Exception ex) {
            System.out.println(String.format("---[LOGIN] Excepting in PlayerLoginModule.login() - %s", ex.getMessage()));
        }
        return spaceship;
    }

    public void logout(ClientConnection connection) {
        if (this.clientConnections.containsKey(connection)) {
            this.clientConnections.remove(connection);
        }
    }

    public void sendLoginError(ClientConnection conn) {
        try {
            PrintWriter writer = new PrintWriter(conn.getSocket().getOutputStream());
            LoginCommError er = new LoginCommError();
            String json = Serializer.serializeLoginCommErrorAsGamePacktet(er);
            
            writer.println(json);
        } catch (IOException ex) {
            System.out.println(String.format("---[LOGIN] Excepting in PlayerLoginModule.sendLoginError() - %s", ex.getMessage()));
        }
    }
}

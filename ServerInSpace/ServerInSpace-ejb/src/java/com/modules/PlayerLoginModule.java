
package com.modules;

import com.google.gson.stream.JsonReader;
import com.objects.Spaceship;
import com.objects.User;
import com.server.DbConnector;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class PlayerLoginModule {
    
    //private Map<User, ClientConnection> clientConnections;
    
    private final ClientConnectionModule server;
    
    public PlayerLoginModule(ClientConnectionModule clientConnectionModule){
        server = clientConnectionModule;
    }
    
    public synchronized boolean login(JsonReader reader, ClientConnection connection){
        boolean isLoggedIn = false;
        String username = null;
        String password = null;
        try {
            reader.beginObject();
            while(reader.hasNext()){
                String tagName = reader.nextName();
                switch(tagName){
                    case "username":
                        username = reader.nextString();
                        break;

                    case "password":
                        password = reader.nextString();
                        break;
                    
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            
           String checkPassword=null;// = DbConnector.identifyUser(username);
           
           if(checkPassword != null && checkPassword.equals(username)){
               //Get Spaceship from user
               Spaceship spaceship = null;// DbConnector.getSpaceship(username);
               
               //Add the spaceship to the location module
               this.server.addSpaceship(spaceship, connection);
               
               //Set logged in true
               isLoggedIn = true;
           }
        } catch (IOException ex) {
            System.out.println("Error while logging in");
            System.out.println(String.format("PlayerLoginModule.login() : %s", ex.getMessage()));
        } finally {
            //Always return the login request
            return isLoggedIn;
        }
    }
    
}

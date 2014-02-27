/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.communication;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceclient.dao.interfaces.DrawCallback;
import spaceclient.game.GameObject;
import spaceclient.game.Spaceship;
import spaceclient.gui.SpaceClient;

/**
 *
 * @author Tim
 */
public class BroadcastHandler implements Runnable {

    private DrawCallback callBack;
    private Communicator communicator;
    private String retrievedJson;
    private static final Gson gson = new Gson();

    public BroadcastHandler(DrawCallback callBack, Spaceship spaceship) {
        System.out.println("initiating constructor");
        this.callBack = callBack;
    }

    @Override
    public void run() {
        System.out.println("initiating communicator");
        communicator = new Communicator();
        communicator.initiate();
        login("asdf", "asdf");
        while (true) {
            handleData();
        }
    }

    public void login(String username, String password){
        LoginComm lc = new LoginComm(LoginComm.class.getSimpleName(), username, password);
        communicator.login(Serializer.serializeLogin(lc));
        
    }
    public void sendData(Spaceship spaceship) {
        communicator.sendData(Serializer.serializeSpaceship(spaceship));
    }

    private void handleData() {
        try {
            System.out.println("In handle data");
            retrievedJson = communicator.retrieveData();
            System.out.println("Recieved: " + retrievedJson);
            if (retrievedJson != null && !retrievedJson.equals("")) {
                System.out.println("Got something");
                GameObject sToAdd = Serializer.desirializePacket(gson.toJsonTree(retrievedJson));
                callBack.drawAfterDataReadFromSocketFromServer((Spaceship) sToAdd);
            }
            System.out.println("finished handledata()");
        } catch (IOException ex) {
            Logger.getLogger(SpaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.communication;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
    private List<GameObject> retrievedObjectList;
    private static final Gson gson = new Gson();

    public BroadcastHandler(DrawCallback callBack, Spaceship spaceship) {
        this.callBack = callBack;
    }

    @Override
    public void run() {
        communicator = new Communicator();
        //communicator.initiate();
        login("asdf2", "asdf2");
        while (true) {
            handleData();
        }
    }

    public void login(String username, String password) {
        LoginComm lc = new LoginComm(LoginComm.class.getSimpleName(), username, password);
        communicator.login(Serializer.serializeLogin(lc));

    }

    public void sendData(Spaceship spaceship) {
        String json = Serializer.serializeSpaceship(spaceship);
        if (communicator != null) {
            communicator.sendData(json);
        }
    }

    private void handleData() {
        try {
            retrievedObjectList = communicator.retrieveData();
            if (retrievedObjectList != null) {
                //GameObject sToAdd = Serializer.desirializePacket(gson.toJsonTree(retrievedJson));
                callBack.drawAfterDataReadFromSocketFromServer(retrievedObjectList);
            }
        } catch (IOException ex) {
            Logger.getLogger(SpaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

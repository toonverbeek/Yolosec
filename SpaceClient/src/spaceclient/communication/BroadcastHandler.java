/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.communication;

import shared.LoginComm;
import shared.Serializer;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.AsteroidComm;
import spaceclient.dao.interfaces.DrawCallback;
import shared.GameObject;
import shared.SpaceshipComm;
import spaceclient.game.Asteroid;
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

    public void sendData(GameObject gObject) {
        String json = "";
        if (gObject instanceof Spaceship) {
            Spaceship s = (Spaceship) gObject;
            json = Serializer.serializeSpaceShipAsGamePacket(SpaceshipComm.class.getSimpleName(), s.getPosition().x, s.getPosition().y, 1, s.getId(), s.getResources());
        } else if (gObject instanceof Asteroid) {
            Asteroid asteroid = (Asteroid) gObject;
            json = Serializer.serializeAsteroidAsGamePacket(AsteroidComm.class.getSimpleName(), asteroid.getType(), asteroid.getResourceAmount(), (int) asteroid.getX(), (int) asteroid.getY());
            System.out.println("Asteroid as gamepacket: " + json);
        }
        if (communicator != null && !json.equals("")) {
            communicator.sendData(json);
        }
    }

    private void handleData() {
        try {
            retrievedObjectList = communicator.retrieveData();
            if (retrievedObjectList != null) {
                System.out.println(retrievedObjectList);
                //GameObject sToAdd = Serializer.desirializePacket(gson.toJsonTree(retrievedJson));
                callBack.drawAfterDataReadFromSocketFromServer(retrievedObjectList);
            }
        } catch (IOException ex) {
            Logger.getLogger(SpaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

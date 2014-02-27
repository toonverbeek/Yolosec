/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.communication;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.geom.Rectangle;
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
    private Spaceship spaceship;
    private static final Gson gson = new Gson();

    public BroadcastHandler(DrawCallback callBack, Spaceship spaceship) {
        System.out.println("initiating constructor");
        this.callBack = callBack;
        this.spaceship = spaceship;
    }

    @Override
    public void run() {
        System.out.println("initiating communicator");
        communicator = new Communicator();
        communicator.initiate();
        handleData();
    }

    private void handleData() {
        try {
            communicator.sendData(Serializer.serializeSpaceship(spaceship));
            retrievedJson = communicator.retrieveData();
            System.out.println(retrievedJson);
            if (retrievedJson != null && !retrievedJson.equals("")) {
                GameObject sToAdd = Serializer.desirializePacket(gson.toJsonTree(retrievedJson));
                callBack.drawAfterDataReadFromSocketFromServer((Spaceship) sToAdd);
            }
        } catch (IOException ex) {
            Logger.getLogger(SpaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

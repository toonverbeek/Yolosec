/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.communication;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.geom.Rectangle;
import spaceclient.dao.interfaces.DrawCallback;
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

    public BroadcastHandler(DrawCallback callBack, Spaceship spaceship) {
        this.callBack = callBack;
        communicator = new Communicator();
        communicator.initiate();
        this.spaceship = spaceship;
    }

    @Override
    public void run() {
        handleData();
    }

    private void handleData() {
        try {
            communicator.sendData(Serializer.serializeSpaceship(spaceship));
            retrievedJson = communicator.retrieveData();
            System.out.println(retrievedJson);
            if (retrievedJson != null && !retrievedJson.equals("")) {
                Spaceship sToAdd = Serializer.deserializeSpaceship(retrievedJson);
                callBack.drawAfterDataReadFromSocketFromServer(sToAdd);
            }
        } catch (IOException ex) {
            Logger.getLogger(SpaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

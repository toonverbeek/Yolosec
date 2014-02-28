/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.communication;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.List;
import java.util.Map;
import spaceclient.game.GameObject;

/**
 *
 * @author Tim
 */
public class Communicator {

    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static Gson gson = new Gson();

    public Communicator() {
        initiate();
    }

    public void sendData(String json) {
        writer.println(json);
    }

    public List<GameObject> retrieveData() throws IOException {
        JsonReader jreader = new JsonReader(new InputStreamReader(socket.getInputStream()));
        jreader.setLenient(true);
        return Serializer.deserializePackets(jreader);

    }

    public void login(String json) {
        writer.println(json);
    }

    public boolean initiate() {
        try {
            socket = new Socket("145.93.56.202", 1337);
            //socket = new Socket("localhost", 1337);
            writer = new PrintWriter(socket.getOutputStream(),
                    true);
            reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            return true;
        } catch (UnknownHostException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

}

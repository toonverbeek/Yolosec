/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;

/**
 *
 * @author Tim
 */
public class Communicator {

    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;

    public Communicator() {
        
    }
    
    public void sendData(String json) {
        writer.println(json);
    }
    
    public String retrieveData() throws IOException {
        return reader.readLine();
    }
    
    public boolean initiate() {
        try {
            socket = new Socket("145.93.58.255", 1337);
            System.out.println("Trying to connect");
            //socket = new Socket("localhost", 1337);
            writer = new PrintWriter(socket.getOutputStream(),
                    true);
            reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            return true;
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: kq6py");
            return false;
        } catch (IOException e) {
            System.out.println("No I/O");
            return false;
        }
    }

}

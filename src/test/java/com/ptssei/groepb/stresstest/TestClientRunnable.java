package com.ptssei.groepb.stresstest;

import com.google.gson.stream.JsonReader;
import com.ptsesd.groepb.shared.GamePacket;
import com.ptsesd.groepb.shared.LoginComm;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.SpaceshipComm;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 *
 * @author user
 */
public class TestClientRunnable implements Callable {

    private Socket socket;
    private PrintWriter writer;
    private JsonReader jreader;

    private final String username;
    private final String IP_ADDRESS;
    private final int id;
    private int timeout;

    public boolean loggedIn = false;

    public TestClientRunnable(String username, int id, String ip) {
        this.username = username;
        this.IP_ADDRESS = ip;
        this.id = id;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public Boolean call() throws Exception {
        if (this.initiate()) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    LoginComm lc = new LoginComm(LoginComm.class.getSimpleName(), username, username);
                    String json = Serializer.serializeLogin(lc);
                    writer.println(json);
                    try {
                        jreader = new JsonReader(new InputStreamReader(socket.getInputStream()));
                        jreader.setLenient(true);

                        GamePacket gp = Serializer.getSingleGamePacket(jreader);
                        if (gp instanceof SpaceshipComm && ((SpaceshipComm) gp).getId() == id) {
                            loggedIn = true;
                            System.out.println(String.format("Login succesfull: %s", id));
                        } else {
                            System.err.println(String.format("Login failed, package header: %s id: %s", gp.getHeader(), id));
                        }
                        closeSocket();
                    } catch (IOException e) {
                        System.err.println(String.format("---[RUNNABLE] Exception in client %s - %s", id, e.getMessage()));
                    }
                }
            });
            t.start();
            Thread.sleep(timeout);
            return loggedIn;
        }
        return false;
    }

    private boolean initiate() {
        try {
            socket = new Socket(IP_ADDRESS, 1337);
            writer = new PrintWriter(socket.getOutputStream(), true);
            return true;
        } catch (IOException e) {
            System.err.println(String.format("Exception in TestClientRunnable.initiate() - %s", e.getMessage()));
        }
        return false;
    }

    public boolean closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}

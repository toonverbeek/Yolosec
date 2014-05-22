package com.yolosec.util;

import com.ptsesd.groepb.shared.MessagingComm;
import com.ptsesd.groepb.shared.SpaceshipComm;
import com.yolosec.service.GameService;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class ConsoleApp implements Runnable {

    private final Boolean isRunning;
    private final GameService connServer;

    public ConsoleApp(GameService connServer) {
        this.connServer = connServer;
        this.printCommands();
        isRunning = true;
    }

    @Override
    public void run() {
        String input = "";
        while (isRunning) {
            Scanner sc = new Scanner(System.in);
            input = sc.next();
            switch (input) {
                case "g":
                case "getStatus":
                    System.out.println(connServer.getStatusInformation());
                    break;
                case "resetAsteroids":
                case "a":
                    System.out.println("---[CONSOLE] Reset Asteroids");
                    this.connServer.resetAsteroids();
                    this.connServer.broadcastAsteroids();
                    break;
                case "getIp":
                case "ip":
                    System.out.println(getIpAddresses());
                    break;
                case "logCpu":
                case "c":
                    System.out.println(logCpuTime());
                    break;
                case "h":
                case "help":
                    printCommands();
                    break;
                case "m":
                case "getMessages":
                    this.getMessages();
                    break;
                case "tm":
                    MessagingComm mcom = new MessagingComm(MessagingComm.class.getSimpleName(), 7, "testmessage", "admin");
                    connServer.receivedMessage(mcom);
                    break;
                case "cm":
                case "clearMessages":
                    this.clearMessages();
                    break;
                case "u":
                case "accountManagement":
                    Thread AccManagementThread = new Thread(new AccountManagementUI());
                    AccManagementThread.start();
                    break;
                case "st":
                case "stresstest":
                    this.stressTestClients();
                    break;
                case "exit":
                    System.out.println("---[CONSOLE] Exit runtime...");
                    //Runtime.getRuntime().exit(0);
                    System.exit(0);
                default:
                    System.out.println("---[CONSOLE] Command not recognized.");
                    break;
            }
        }
    }

    private void printCommands() {
        System.out.println("---[CONSOLE] Starting console...");
        System.out.println("---[CONSOLE] Available commands are:");
        System.out.println("---[CONSOLE] -getIp [-ip]");
        System.out.println("---[CONSOLE] -getMessages [-m]");
        System.out.println("---[CONSOLE] -clearMessages [-cm]");
        System.out.println("---[CONSOLE] -getStatus [-g]");
        System.out.println("---[CONSOLE] -resetAsteroids [-a]");
        System.out.println("---[CONSOLE] -accountManagement [-u]");
        System.out.println("---[CONSOLE] -logCpu [-c]");
        System.out.println("---[CONSOLE] -stresstest [-st]");
        System.out.println("---[CONSOLE] -help [-h]");
        System.out.println("---[CONSOLE] -exit");
        System.out.println("------------------------------------");
    }

    private String getIpAddresses() {
        String concat = "";
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                if (n.getInetAddresses().hasMoreElements()) {
                    String ip = n.getInetAddresses().nextElement().getHostAddress();
                    if (!ip.contains(":")) {
                        concat = concat.concat("IP: " + ip + '\n');
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println(ex.getMessage());
        }
        return concat;
    }

    private String logCpuTime() {
        Boolean started = connServer.logCpuTime();
        if (started) {
            return "CPU logging enabled.";
        } else {
            return "CPU logging disabled.";
        }
    }

    private void getMessages() {
        this.connServer.getMessagesFromServer();
        List<MessagingComm> messages = this.connServer.getMessages();

        for (MessagingComm item : messages) {
            String username = item.getUsername();
            String message = item.getMessage();
            Date timestamp = item.getTimestamp();
            System.out.println(String.format("-[CHAT] %s (%s): %s", username, timestamp.toString(), message));
        }
    }

    private void clearMessages() {
        boolean success = this.connServer.clearMessages();
        System.out.println(String.format("---[CONSOLE] Messages cleared: %s", success));
    }

    private void stressTestClients() {
        final int USERIDBASE = 2000000000;
        final int CLIENTS = 100;
        //SaceshipServiceImpl - Map<Integer, SpaceshipComm> clientSpaceships
        ArrayList<SpaceshipComm> usedShips = new ArrayList<>();
        Random r = new Random();
        int nn[] = {0, 0, 0};

        //create a list with temporary users
        for (int i = 0; i < CLIENTS; i++) {
            usedShips.add(new SpaceshipComm("testuser" + i, USERIDBASE + i, r.nextInt(65535), r.nextInt(65535), 1, nn, false));
        }
        //add the users to the active list
        for (SpaceshipComm ship : usedShips) {
            connServer.addSpaceShipObject(ship);
        }

        try {
            Thread.sleep(60000); //sleep for 1 minute
        } catch (InterruptedException ex) {
            System.out.println("---[STRESSTEST] Sleep InterruptedException");
        }
        //remove the users from the active list
        for (SpaceshipComm ship : usedShips) {
            connServer.removeSpaceshipObject(ship.getId());
        }
    }
}

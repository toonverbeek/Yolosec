package com.yolosec.util;

import com.yolosec.service.GameService;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
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
        System.out.println("---[CONSOLE] Starting console...");
        System.out.println("---[CONSOLE] Available commands are:");
        System.out.println("---[CONSOLE] -getStatus [-g]");
        System.out.println("---[CONSOLE] -resetAsteroids [-a]");
        System.out.println("---[CONSOLE] -getIp [-ip]");
        System.out.println("---[CONSOLE] -logCpu [-c]");
        System.out.println("---[CONSOLE] -exit");
        System.out.println("------------------------------------");
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
                    break;
                case "getIp":
                case "ip":
                    System.out.println(getIpAddresses());
                    break;
                case "logCpu":
                case "c":
                    System.out.println(logCpuTime());
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
}

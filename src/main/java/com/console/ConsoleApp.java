package com.console;

import com.server.ConnectionServer;
import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class ConsoleApp implements Runnable {
    
    private final Boolean isRunning;
    private final ConnectionServer connServer;
    
    public ConsoleApp(ConnectionServer connServer) {
        this.connServer = connServer;
        System.out.println("---[CONSOLE] Starting console...");
        System.out.println("---[CONSOLE] Available commands are:");
        System.out.println("---[CONSOLE] -getStatus [-g]");
        System.out.println("---[CONSOLE] -resetAsteroids [-a]");
        System.out.println("---[CONSOLE] -exit");
        System.out.println("------------------------------------");
        isRunning = true;
    }

    @Override
    public void run() {
        String input = "";
        while(isRunning) {
            Scanner sc = new Scanner(System.in);
            input = sc.next();
            switch(input) {
                case "g":
                    System.out.println(connServer.getStatusInformation());
                    break;
                case "getStatus":
                    System.out.println(connServer.getStatusInformation());
                    break;
                case "resetAsteroids":
                    System.out.println("---[CONSOLE] Reset Asteroids");
                    this.connServer.resetAsteroids();
                    break;
                case "a":
                    System.out.println("---[CONSOLE] Reset Asteroids");
                    this.connServer.resetAsteroids();
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
    
}

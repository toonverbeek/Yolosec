package com.console;

import com.modules.ConnectionServer;
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
        System.out.println("Starting console...");
        System.out.println("Available commands are:");
        System.out.println("-getStatus");
        System.out.println("-exit");
        System.out.println("\n-----------------");
        isRunning = true;
    }

    @Override
    public void run() {
        String input = "";
        while(isRunning) {
            Scanner sc = new Scanner(System.in);
            input = sc.next();
            switch(input) {
                case "getStatus":
                    System.out.println(connServer.getStatusInformation());
                    break;
                case "exit":
                    System.out.println("Exit runtime...");
                    //Runtime.getRuntime().exit(0);
                    System.exit(0);
                default:
                    System.out.println("Command not recognized.");
                    break;
            }
        }
    }
    
}

package com.console;

import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class ConsoleApp {
    
    private final Boolean isRunning;
    public ConsoleApp() {
        System.out.println("Starting console...");
        System.out.println("Available commands are:");
        System.out.println("-getStatus");
        System.out.println("-exit");
        isRunning = true;
        init();
    }
    
    private void init(){
        String input = "";
        while(isRunning) {
            Scanner sc = new Scanner(System.in);
            input = sc.next();
            switch(input) {
                case "getStatus":
                    System.out.println("((getstatus))");
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

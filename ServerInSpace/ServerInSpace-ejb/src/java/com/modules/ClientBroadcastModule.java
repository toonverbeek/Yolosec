
package com.modules;

/**
 *
 * @author user
 */
public class ClientBroadcastModule implements Runnable{
    
    private boolean isRunning = false;
    private final ConnectionServer connectionModule;
    
    public ClientBroadcastModule(ConnectionServer connectionModule){
        this.connectionModule = connectionModule;
        this.isRunning = true;
    }
    
    @Override
    public synchronized void run() {
        while (isRunning) {
            try {
                connectionModule.broadcastPositions();
                //System.out.println("broadcast");
                this.wait(16);
            } catch (InterruptedException ex) {
                System.out.println(String.format("ClientBroadcastModule is interrupted : %s", ex.getMessage()));
            }
        }
    }
    
    
}

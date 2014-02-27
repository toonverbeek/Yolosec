
package com.server;

import com.modules.ClientConnectionModule;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class ServerStatusModule implements Runnable{
    
    private final ClientConnectionModule clientConnectionModule;
    private boolean showInformation = false;
    
    public ServerStatusModule(ClientConnectionModule clientConnectionModule) {
        this.clientConnectionModule = clientConnectionModule;
        this.showInformation = true;
    }
    
    @Override
    public synchronized void run() {
        while(showInformation){
            try {
                //clientConnectionModule.get
                System.out.println(clientConnectionModule.getStatusInformation());
                //System.out.println("broadcast");
                this.wait(5000);
            } catch (InterruptedException ex) {
                System.out.println(String.format("ClientBroadcastModule is interrupted : %s", ex.getMessage()));
            }
        }
    }
    
}

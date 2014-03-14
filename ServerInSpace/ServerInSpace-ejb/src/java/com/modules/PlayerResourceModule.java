
package com.modules;

import com.server.DbConnector;
import java.util.Map;
import shared.SpaceshipComm;

/**
 *
 * @author user
 */
public class PlayerResourceModule {
    PlayerLocationModule playerModule;
    
    public PlayerResourceModule(PlayerLocationModule playerModule){
        this.playerModule = playerModule;
    }
    
    public void addResourceToClient(ClientConnection conn, int newResources){
        int clientID = playerModule.getClientSpaceship(conn).getId();
        int resourceAmount = DbConnector.getSpaceshipResourceAmount(clientID);
        resourceAmount += newResources;
        
        DbConnector.setSpaceshipResourceAmount(clientID, resourceAmount);
    }
}

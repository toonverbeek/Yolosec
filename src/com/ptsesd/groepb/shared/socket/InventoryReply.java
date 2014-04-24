/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared.socket;

import com.ptsesd.groepb.shared.GamePacket;
import com.ptsesd.groepb.shared.ItemComm;
import java.util.List;

/**
 *
 * @author user
 */
public class InventoryReply extends GamePacket{
    
    private final List<ItemComm> items;
    private final int spaceshipId;
    
    public InventoryReply(String header, int spaceshipId, List<ItemComm> items){
        super(header);
        this.spaceshipId = spaceshipId;
        this.items = items;
    }

    public int getSpaceshipId() {
        return spaceshipId;
    }

    public List<ItemComm> getItems() {
        return items;
    }
}

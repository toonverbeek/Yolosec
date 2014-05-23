/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared.socket;

import com.google.gson.annotations.Expose;
import com.ptsesd.groepb.shared.GamePacket;
import com.ptsesd.groepb.shared.ItemComm;
import java.util.List;

/**
 *
 * @author user
 */
public class InventoryReply extends GamePacket{
    
    @Expose private final List<ItemComm> items;
    @Expose private final long spaceshipId;
    @Expose private final boolean isAuctionHouse;
    
    public InventoryReply(String header, long spaceshipId, List<ItemComm> items, boolean isAuctionHouse){
        super(header);
        this.spaceshipId = spaceshipId;
        this.items = items;
        this.isAuctionHouse = isAuctionHouse;
    }

    public long getSpaceshipId() {
        return spaceshipId;
    }

    public List<ItemComm> getItems() {
        return items;
    }

    public boolean isAuctionHouse() {
        return isAuctionHouse;
    }
}

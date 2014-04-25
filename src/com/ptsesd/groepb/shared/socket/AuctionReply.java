/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared.socket;

import com.ptsesd.groepb.shared.AuctionHouseRequestType;
import com.ptsesd.groepb.shared.GamePacket;

/**
 *
 * @author user
 */
public class AuctionReply extends GamePacket{
    
    private final int itemId;
    private final AuctionHouseRequestType type;
    
    public AuctionReply(String header, int itemId, AuctionHouseRequestType type){
        super(header);
        this.itemId = itemId;
        this.type = type;
    }

    public int getItemId() {
        return itemId;
    }

    public AuctionHouseRequestType getType() {
        return type;
    }
}

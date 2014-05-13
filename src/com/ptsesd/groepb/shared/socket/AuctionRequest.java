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
public class AuctionRequest extends GamePacket{
    
    private final int userId;
    private final long itemId;
    private final AuctionHouseRequestType type;
    
    public AuctionRequest(String header, int userId, long itemId, AuctionHouseRequestType type){
        super(header);
        this.userId = userId;
        this.itemId = itemId;
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public long getItemId() {
        return itemId;
    }

    public AuctionHouseRequestType getType() {
        return type;
    }
}

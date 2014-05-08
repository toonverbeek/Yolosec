/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yolosec.spaceclient.game.player;

import com.ptsesd.groepb.shared.AuctionHouseRequestType;
import com.ptsesd.groepb.shared.ItemComm;

/**
 *
 * @author Toon
 */
public class Item {
    
    private final Long itemId;
    private final int sellerId;
    private final AuctionHouseRequestType requestType;
    private final int resourceAmount;
    private final String resourceType;

    public Item(ItemComm ic){
        this.itemId = ic.getItemId();
        this.sellerId = ic.getSellerId();
        this.requestType = ic.getRequestType();
        this.resourceAmount = ic.getResourceAmount();
        this.resourceType = ic.getResourceType();
    }
    
    public Item(Long itemId, int sellerId, int resourceAmount, String resourceType, AuctionHouseRequestType requestType) {
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.requestType = requestType;
        this.resourceAmount = resourceAmount;
        this.resourceType = resourceType;
    }

    public AuctionHouseRequestType getRequestType() {
        return requestType;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }

    public String getResourceType() {
        return resourceType;
    }

    public Long getItemId() {
        return itemId;
    }

    public int getSellerId() {
        return sellerId;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptsesd.groepb.shared;

/**
 *
 * @author Tim
 */
public class ItemComm {

    private final Long itemId;
    private final int sellerId;
    private final AuctionHouseRequestType requestType;
    private final int resourceAmount;
    private final String resourceType;

    public ItemComm(Long itemId, int sellerId, int resourceAmount, String resourceType, AuctionHouseRequestType requestType) {
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

package com.ptsesd.groepb.shared;

import java.util.List;

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
    private final String name;
    private final String imageName;
    private final List<Object> propertyList;

    public ItemComm(Long itemId, int sellerId, String name, int resourceAmount, String resourceType, AuctionHouseRequestType requestType) {
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.name = name;
        this.requestType = requestType;
        this.resourceAmount = resourceAmount;
        this.resourceType = resourceType;
        this.imageName = "";
        this.propertyList = null;
    }

    public ItemComm(Long itemId, int sellerId, String name, int resourceAmount, String resourceType, AuctionHouseRequestType requestType, String imageName, List<Object> propertyList) {
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.name = name;
        this.requestType = requestType;
        this.resourceAmount = resourceAmount;
        this.resourceType = resourceType;
        this.imageName = imageName;
        this.propertyList = propertyList;
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

    public String getName() {
        return this.name;
    }

    public String getImageName() {
        return this.imageName;
    }

    public List<Object> getPropertyList() {
        return this.propertyList;
    }
}

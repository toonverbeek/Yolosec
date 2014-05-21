package com.ptsesd.groepb.shared;

import java.util.List;

/**
 *
 * @author Tim
 */
public class ItemComm {

    private final Item item;
    private final long requestorId;
    private final AuctionHouseRequestType requestType;

    public ItemComm(Item item, long requestorId, AuctionHouseRequestType requestType) {
        this.item = item;
        this.requestorId = requestorId;
        this.requestType = requestType;
    }

    public Item getItem() {
        return item;
    }

    public long getRequestorId() {
        return requestorId;
    }

    public AuctionHouseRequestType getRequestType() {
        return requestType;
    }
}

package com.ptsesd.groepb.shared;

import com.google.gson.annotations.Expose;
import java.util.List;

/**
 *
 * @author Tim
 */
public class ItemComm extends GamePacket {

    @Expose private Item item;
    @Expose private final long requestorId;
    @Expose private final AuctionHouseRequestType requestType;
    @Expose private int itemId;

    public ItemComm(Item item, long requestorId, AuctionHouseRequestType requestType) {
        this.item = item;
        this.requestorId = requestorId;
        this.requestType = requestType;
    }

    public ItemComm(int itemid, long requestorId, AuctionHouseRequestType requestType) {
        this.itemId = itemid;
        this.requestorId = requestorId;
        this.requestType = requestType;
    }

    public Item getItem() {
        return item;
    }
    
    public int getItemId(){
        return itemId;
    }

    public long getRequestorId() {
        return requestorId;
    }

    public AuctionHouseRequestType getRequestType() {
        return requestType;
    }
}

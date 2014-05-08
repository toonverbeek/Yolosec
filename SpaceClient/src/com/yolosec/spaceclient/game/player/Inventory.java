/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game.player;

import com.google.gson.internal.LinkedTreeMap;
import com.ptsesd.groepb.shared.AuctionHouseRequestType;
import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.socket.InventoryReply;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Lisanne
 */
public class Inventory extends GameObjectImpl {

    private int spaceshipId;
    private ArrayList<Item> items;

    public Inventory(InventoryReply ir) {
        this.spaceshipId = ir.getSpaceshipId();
        items = new ArrayList<>();
        for(Object item : ir.getItems()){
            Map itemlist = (LinkedTreeMap) item;
            Long itemid = ((Double) itemlist.get("itemId")).longValue();
            int sellerId = ((Double)itemlist.get("sellerId")).intValue();
            int resourceAmount = ((Double)itemlist.get("resourceAmount")).intValue();
            String resourceType = (String)itemlist.get("resourceType");
            AuctionHouseRequestType requestType = AuctionHouseRequestType.valueOf((String)itemlist.get("requestType"));
            
            Item itemToAdd = new Item(itemid, sellerId, resourceAmount, resourceType, requestType);
            items.add(itemToAdd);
        }
    }

    public int getSpaceshipId() {
        return spaceshipId;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
    
    @Override
    public Rectangle getRectangle() {
        return new Rectangle();
    }

}

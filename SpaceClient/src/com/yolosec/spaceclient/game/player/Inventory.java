/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game.player;

import com.google.gson.internal.LinkedTreeMap;
import com.ptsesd.groepb.shared.AuctionHouseRequestType;
import com.ptsesd.groepb.shared.Item;
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

    private long spaceshipId;
    private ArrayList<ItemComm> items;
    private boolean isAuctionHouse;

    public Inventory(InventoryReply ir) {
        this.spaceshipId = ir.getSpaceshipId();
        this.isAuctionHouse = ir.isAuctionHouse();
        items = new ArrayList<>();
        for (Object item : ir.getItems()) {
            Map itemlist2 = (LinkedTreeMap) item;
            if (itemlist2 != null) {
                Map itemlist = (Map) itemlist2.get("item");
                Long itemid = ((Double) itemlist.get("id")).longValue();
                String name = (String) itemlist.get("name");
                String description = (String) itemlist.get("description");
                String image = (String) itemlist.get("image");
                float value = ((Double) itemlist.get("value")).floatValue();
                String resourceType = (String) itemlist.get("resource_type");
                Item itemToAdd = new Item(itemid, name, description, image, value, resourceType);
                AuctionHouseRequestType requestType = null;
                if (isAuctionHouse) {
                    requestType = (AuctionHouseRequestType.valueOf((String) itemlist2.get("requestType")));
                    System.out.println("AUCTIONHOUSE: " + requestType);
                }
                ItemComm ic = new ItemComm(itemToAdd, spaceshipId, requestType);
                items.add(ic);
            }
        }
    }

    public long getSpaceshipId() {
        return spaceshipId;
    }

    public ArrayList<ItemComm> getItems() {
        return items;
    }

    public boolean isAuctionHouse() {
        return isAuctionHouse;
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle();
    }

}

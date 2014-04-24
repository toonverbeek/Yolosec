/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yolosec.data;

import com.ptsesd.groepb.shared.AuctionHouseRequestType;
import com.ptsesd.groepb.shared.ItemComm;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class ItemDAOImpl implements ItemDAO {

    @Override
    public List<ItemComm> getInventory(int spaceshipId) {
        //JMS Connect to the Economy system
        //Get the list from the messaging gateway
        //Return the list
        List<ItemComm> mockItems = new ArrayList<>();
        mockItems.add(new ItemComm(1L, 1, 20, "common", AuctionHouseRequestType.CANCEL));
        mockItems.add(new ItemComm(2L, 2, 50, "magic", AuctionHouseRequestType.BUY));
        mockItems.add(new ItemComm(3L, 2, 5000, "common", AuctionHouseRequestType.SELL));
        return mockItems;
        
    }

    @Override
    public List<ItemComm> getAuctionHouse() {
        List<ItemComm> mockItems = new ArrayList<>();
        mockItems.add(new ItemComm(1L, 1, 20, "common", AuctionHouseRequestType.CANCEL));
        mockItems.add(new ItemComm(2L, 2, 50, "magic", AuctionHouseRequestType.BUY));
        mockItems.add(new ItemComm(3L, 2, 5000, "common", AuctionHouseRequestType.SELL));
        return mockItems;
    }

    @Override
    public void buyItem(int spaceshipId, int itemId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sellItem(int spaceshipId, int itemId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancelAuction(int spaceshipId, int itemId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

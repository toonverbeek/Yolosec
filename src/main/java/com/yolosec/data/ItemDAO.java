/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yolosec.data;

import com.ptsesd.groepb.shared.ItemComm;
import java.util.List;

/**
 *
 * @author user
 */
public interface ItemDAO {
    
    public List<ItemComm> getInventory(int spaceshipId);
    
    public List<ItemComm> getAuctionHouse();
    
    public void buyItem(int spaceshipId, int itemId);
    
    public void sellItem(int spaceshipId, int itemId);
    
    public void cancelAuction(int spaceshipId, int itemId);
}

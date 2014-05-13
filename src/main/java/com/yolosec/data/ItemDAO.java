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
    
    public void buyItem(int spaceshipId, long itemId);
    
    public void sellItem(int spaceshipId, long itemId);
    
    public void cancelAuction(int spaceshipId, long itemId);
}

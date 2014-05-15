package com.yolosec.data;

import com.ptsesd.groepb.shared.AuctionHouseRequestType;
import com.ptsesd.groepb.shared.ItemComm;
import com.yolosec.jms.EconomyImpl;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class ItemDAOImpl implements ItemDAO {

    EconomyImpl economy;

    public ItemDAOImpl() {
        this.economy = new EconomyImpl();
    }

    @Override
    public List<ItemComm> getInventory(int spaceshipId) {
        //JMS Connect to the Economy system
        //Get the list from the messaging gateway
        //Return the list
        List<ItemComm> mockItems = new ArrayList<>();
        mockItems.add(new ItemComm(1L, 1, "name", 20, "common", AuctionHouseRequestType.CANCEL));
        mockItems.add(new ItemComm(1L, 2, "name", 20, "common", AuctionHouseRequestType.CANCEL));
        mockItems.add(new ItemComm(2L, 1, "name", 50, "magic", AuctionHouseRequestType.BUY));
        mockItems.add(new ItemComm(2L, 2, "name", 50, "magic", AuctionHouseRequestType.BUY));
        mockItems.add(new ItemComm(3L, 1, "name", 5000, "common", AuctionHouseRequestType.SELL));
        mockItems.add(new ItemComm(3L, 2, "name", 5000, "common", AuctionHouseRequestType.SELL));
        return mockItems;

    }

    @Override
    public List<ItemComm> getAuctionHouse() {
        List<ItemComm> mockItems = new ArrayList<>();
//        mockItems.add(new ItemComm(1L, 1, 20, "common", AuctionHouseRequestType.CANCEL));
//        mockItems.add(new ItemComm(2L, 2, 50, "magic", AuctionHouseRequestType.BUY));
//        mockItems.add(new ItemComm(3L, 2, 5000, "common", AuctionHouseRequestType.SELL));
//        mockItems.add(new ItemComm(4L, 2, 500, "maaaaagic", AuctionHouseRequestType.SELL));

        for (int i = 1; i < 10; i++) {
            List<Object> l = new ArrayList<>();
            mockItems.add(new ItemComm((long) i, 2, "itemname", 100 * i, "maaaaagic", AuctionHouseRequestType.SELL, "imagelocation", l));
        }
        return mockItems;
    }

    @Override
    public void buyItem(int spaceshipId, long itemId) {
        for (ItemComm i : getAuctionHouse()) {
            if (i.getItemId() == itemId) {
                economy.processItem(spaceshipId, i);
                break;
            }
        }
    }

    @Override
    public void sellItem(int spaceshipId, long itemId) {
        for (ItemComm i : getInventory(spaceshipId)) {
            if (i.getItemId() == itemId) {
                economy.processItem(spaceshipId, i);
                break;
            }
        }
    }

    @Override
    public void cancelAuction(int spaceshipId, long itemId) {
        for (ItemComm i : getAuctionHouse()) {
            if (i.getItemId() == itemId) {
                economy.processItem(spaceshipId, i);
                break;
            }
        }
    }
}

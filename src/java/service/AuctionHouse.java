/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.google.gson.JsonObject;
import com.ptsesd.groepb.shared.ItemComm;
import dao.AuctionHouseItemDAO;
import dao.AuctionHouseItem_JPAImpl;
import dao.ItemDAO;
import dao.ItemDAO_JPAImpl;
import domain.AuctionHouseItem;
import domain.Item;

/**
 *
 * @author Jamy
 */
public class AuctionHouse {

    private final AuctionHouseItemDAO ahItemDao;
    private final ItemDAO itemDAO;

    /**
     * Creates a new instance of type AuctionHouse. The AuctionHouse provides an
     * interface that enables a user to buy from and sell to other users.
     */
    public AuctionHouse() {
        ahItemDao = new AuctionHouseItem_JPAImpl();
        itemDAO = new ItemDAO_JPAImpl();
    }

    /**
     * Gets an AuctionHouseItem from the database using the DAO.
     *
     * @param ahItemId the AuctionHouseItem Id for the AuctionHouseItem that is
     * going to be retrieved from the database.
     * @return the AuctionHouseItem.
     */
    AuctionHouseItem getAuctionHouseItem(long ahItemId) {
        return ahItemDao.find(ahItemId);
    }

    /**
     * *
     * Finds an item for sale, removes it from the database and returns an
     * AucitonHouseItem to the caller.
     *
     * @param ahItemId the AuctionHouseItem Id for the AuctionHouseItem that we
     * are about to buy.
     * @return the AuctionHouseItem that has been bought.
     */
    AuctionHouseItem buyItem(long ahItemID) {
        //TODO JMS
        AuctionHouseItem _item = ahItemDao.find(ahItemID);

        ahItemDao.remove(_item);
        return _item;
    }

    /**
     * Puts an item for sale on the AuctionHouse.
     *
     * @param ahItem the AuctionHouseItem that will be posted to the
     * AuctionHouse.
     * @return true if successfully posted to the AuctionHouse, false otherwise.
     */
    boolean putItemForSale(AuctionHouseItem ahItem) {
        if (ahItem.getResourceAmount() > 0) {
            return ahItemDao.create(ahItem);
        } else {
            return false;
        }
    }

    /**
     * 
     * @param incomingItem
     * @return 
     */
    public boolean newBuyItemRequest(ItemComm incomingItem) {

        //find the item corresponding to the item id 
        Item itemForSale = itemDAO.find(incomingItem.getItemId());

        //create  an ah item from the item and put it on the auctionhouse
        AuctionHouseItem ahItemForSale = new AuctionHouseItem(itemForSale, incomingItem.getSellerId(), incomingItem.getResourceAmount(), incomingItem.getResourceType());
        return this.putItemForSale(ahItemForSale);
    }

    public boolean newItemForSaleRequest(ItemComm incomingItem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean cancelItemForSaleRequest(ItemComm incomingItem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

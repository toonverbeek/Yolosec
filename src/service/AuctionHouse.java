/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.AuctionHouseItemDAO;
import dao.AuctionHouseItem_JPAImpl;
import domain.AuctionHouseItem;

/**
 *
 * @author Jamy
 */
public class AuctionHouse {

    private final AuctionHouseItemDAO ahItemDao;

    public AuctionHouse() {
        ahItemDao = new AuctionHouseItem_JPAImpl();
    }

    AuctionHouseItem getAuctionHouseItem(long ahItemId) {
        return ahItemDao.find(ahItemId);
    }

    /***
     * 
     * @param ahItemID
     * @return 
     */
    AuctionHouseItem buyItem(long ahItemID) {
       //TODO JMS
        AuctionHouseItem _item = ahItemDao.find(ahItemID);

        ahItemDao.remove(_item);
        return _item;
    }

    boolean addItem(AuctionHouseItem ahItem) {
        if (ahItem.getResourceAmount() > 0) {
            return ahItemDao.create(ahItem);
        } else {
            return false;
        }
    }
}

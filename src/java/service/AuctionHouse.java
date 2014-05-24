/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.ptsesd.groepb.shared.AuctionHouseRequestType;
import com.ptsesd.groepb.shared.Item;
import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.User;
import com.ptsesd.groepb.shared.UserItem;
import dao.ItemDAO;
import dao.ItemDAO_JPAImpl;
import dao.UserDAO;
import dao.UserDAO_Impl;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrator
 */
public class AuctionHouse {

    private ItemDAO itemDAO;
    private UserDAO userDAO;

    public AuctionHouse(EntityManager em) {
        itemDAO = new ItemDAO_JPAImpl(em);
        userDAO = new UserDAO_Impl(em);
    }

    boolean buyItem(ItemComm incomingItem) {
        Item item = itemDAO.find((long) incomingItem.getItemId());
        UserItem uItem = itemDAO.find(item);
        Item dbItem = itemDAO.find(uItem.getItem().getId());
        if (dbItem != null) {
            uItem.setForSale(false);
            User newUser = userDAO.find((long) incomingItem.getRequestorId());
            if (newUser != null) {
                uItem.setUser(newUser);
                itemDAO.edit(uItem);
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    boolean sellItem(ItemComm incomingItem) {
        Item item = itemDAO.find((long) incomingItem.getItemId());
        UserItem uItem = itemDAO.find(item);
        if (uItem != null) {
            uItem.setForSale(true);
            itemDAO.edit(uItem);
            return true;
        } else {
            return false;
        }
    }

    boolean cancelItem(ItemComm incomingItem) {
        Item item = itemDAO.find((long) incomingItem.getItemId());
        UserItem uItem = itemDAO.find(item);
        if (uItem != null) {
            uItem.setForSale(false);
            itemDAO.edit(uItem);
            return true;
        } else {
            return false;
        }
    }

    int[] getResources(long userId) {
        User user = userDAO.find(userId);
        int[] resources = new int[3];
        if (user != null) {
            resources[1] = user.getResource_magic();
            resources[0] = user.getResource_normal();
            resources[2] = user.getResource_rare();
        } else {
            resources[1] = 1;
            resources[0] = 1;
            resources[2] = 1;
        }
        return resources;
    }

    boolean saveResources(long userId, int resource_normal, int resource_magic, int resource_rare) {
        User find = userDAO.find(userId);
        if (find != null) {
            find.setResource_normal(resource_normal);
            find.setResource_magic(resource_magic);
            find.setResource_rare(resource_rare);
            userDAO.edit(find);
            return true;
        }
        return false;
    }

    List<ItemComm> getInventory(long userId) {
        User user = userDAO.find(userId);
        return itemDAO.getInventory(user);
    }

    List<ItemComm> getAuctionHouse(long userId) {
        List<UserItem> allUserItems = itemDAO.findAllUserItem();
        List<ItemComm> auctionHouseItems = new ArrayList<>();

        for (UserItem uItem : allUserItems) {
            ItemComm ic = null;
            Item item = itemDAO.find(uItem.getItem().getId());
            //when i am te owner
            if (uItem.getUser() != null) {
                if (uItem.getUser().getUserId() == userId) {
                    if (uItem.isForSale()) {
                        ic = new ItemComm(item, userId, AuctionHouseRequestType.CANCEL);
                    } else {
                        ic = new ItemComm(item, userId, AuctionHouseRequestType.SELL);
                    }
                } else {
                    if (uItem.isForSale()) {
                        ic = new ItemComm(item, userId, AuctionHouseRequestType.BUY);
                    }
                }
            }
            if (ic != null) {
                auctionHouseItems.add(ic);
            }
        }

        return auctionHouseItems;
    }

}

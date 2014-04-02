/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.AuctionHouseItem;
import domain.Item;
import java.util.List;

/**
 *
 * @author Lisanne
 */
public interface AuctionHouseItemDAO {

    int count();

    boolean create(AuctionHouseItem item);

    void edit(AuctionHouseItem item);

    List<AuctionHouseItem> findAll();

    AuctionHouseItem find(String itemname);

    AuctionHouseItem find(Long id);

    void remove(AuctionHouseItem item);
    
    void closeEntityManager();
}

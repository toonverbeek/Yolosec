/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.ptsesd.groepb.shared.Item;
import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.User;
import com.ptsesd.groepb.shared.UserItem;
import java.util.List;

/**
 *
 * @author Lisanne
 */
public interface ItemDAO {

    int count();

    void create(Item item);

    void edit(Item item);
    
    boolean edit(User user);
    
    void edit(UserItem item);
    
    List<UserItem> findAllUserItem(); 
    
    List<UserItem> findAllUserItem(User user); 

    List<Item> findAll();

    Item find(String itemname);

    Item find(Long id);
    
    UserItem find(Item item);

    void remove(Item item);
    
    void closeEntityManager();
    
    List<ItemComm> getInventory(User user);

    public boolean buyItem(User user, Item item);
}

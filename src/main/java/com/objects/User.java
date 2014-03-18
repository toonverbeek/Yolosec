package com.objects;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class User {
    private final String username;
    private List<Item> inventory;

    public User(String username) {
        this.username = username;
    }

    public List<Item> getInventory() {
        return inventory;
    }
    
    public void AddItem(Item itm) {
        inventory.add(itm);
    }
    
    public void RemoveItem(Item itm) {
        inventory.remove(itm);
    }
}

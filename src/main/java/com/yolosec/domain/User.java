package com.yolosec.domain;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class User {
    private final String username;
    private String password;
    private Integer id;
    private boolean mod;
    
    private List<Item> inventory;

    public User(String username) {
        this.username = username;
    }
    
    public User(String username, String password, Integer id, boolean mod) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.mod = mod;
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
    
    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Integer getId() {
        return this.id;
    }
    
    public boolean getMod() {
        return this.mod;
    }
    
    public void setMod(boolean mod) {
        this.mod = mod;
    }
}

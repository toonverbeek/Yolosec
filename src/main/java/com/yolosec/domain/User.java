package com.yolosec.domain;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class User {

    private String username;
    private String password;
    private Integer id;
    private boolean moderator;
    private Spaceship spaceship;

    private List<Item> inventory;

    public User(String username) {
        this.username = username;
        //spaceship = new Spaceship();
    }

    public User(String username, String password, Integer id, boolean mod) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.moderator = mod;
        //spaceship = new Spaceship();
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

    public void setUsername(String value) {
        this.username = value;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    public Integer getId() {
        return this.id;
    }

    public boolean getMod() {
        return this.moderator;
    }

    public void setMod(boolean mod) {
        this.moderator = mod;
    }
}

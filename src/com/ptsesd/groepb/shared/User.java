/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared;

import com.ptsesd.groepb.shared.Item;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

/**
 *
 * @author Administrator
 */
@Entity
public class User implements Serializable {
    
    @GeneratedValue @Id
    private long id;
    
    @OneToMany(mappedBy = "user")
    private List<UserItem> inventory;
    
    private String username;
    private String password;
    
    private int resource_normal;
    private int resource_magic;
    private int resource_rare;
    
    public User(){
        
    }
    
    public User(List<UserItem> inventory, String username, String password){
        this.inventory = inventory;
        this.username = username;
        this.password = password;
    }

    public void setResource_normal(int resource_normal) {
        this.resource_normal = resource_normal;
    }

    public void setResource_magic(int resource_magic) {
        this.resource_magic = resource_magic;
    }

    public void setResource_rare(int resource_rare) {
        this.resource_rare = resource_rare;
    }

    public int getResource_normal() {
        return resource_normal;
    }

    public int getResource_magic() {
        return resource_magic;
    }

    public int getResource_rare() {
        return resource_rare;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getUserId() {
        return id;
    }

    public void setUserId(long userId) {
        this.id = userId;
    }

    public List<UserItem> getInventory() {
        return inventory;
    }

    public void setInventory(List<UserItem> inventory) {
        this.inventory = inventory;
    }
}

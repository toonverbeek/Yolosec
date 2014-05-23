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
    
    public User(){
        
    }
    
    public User(List<UserItem> inventory, String username, String password){
        this.inventory = inventory;
        this.username = username;
        this.password = password;
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

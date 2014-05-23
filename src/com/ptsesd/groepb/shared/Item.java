/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptsesd.groepb.shared;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Item implements Serializable {

    @Id
    @GeneratedValue
    @Expose private long id;
    @Expose private String name;
    @Expose private String description;
    @Expose private String image;
    @Expose private float value;
    @Expose private String resource_type;
    
    @OneToMany(mappedBy = "item")
    private List<UserItem> users;

    public Item(){
        
    }

    public Item(String name, String description, String image, float value, String resource_type) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.value = value;
        this.resource_type = resource_type;
    }
    
    public Item(long id, String name, String description, String image, float value, String resource_type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.value = value;
        this.resource_type = resource_type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }
}

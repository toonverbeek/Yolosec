/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared;

import java.util.Objects;

;

/**
 *
 * @author Toon
 */
public abstract class GamePacket {
    private Integer id;
    private String header;

    public GamePacket(String header, Integer id) {
        this.header = header;
        this.id = id;
    }
    
    public GamePacket(){
        
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Integer getId() {
        return id;
    }
    
    /**
     * Checks if the ID of the GamePacket is equal to the other's GamePacket
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        GamePacket otherPacket = (GamePacket)o;
        boolean equal = false;
        
        if(this.id == otherPacket.getId()){
            equal = true;
        }
        
        return equal;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    
    
}

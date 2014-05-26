/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared;

import com.google.gson.annotations.Expose;
import java.util.Objects;

;

/**
 *
 * @author Toon
 */
public abstract class GamePacket {
    @Expose private String header;

    public GamePacket(String header) {
        this.header = header;
    }
    
    public GamePacket(){
        
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}

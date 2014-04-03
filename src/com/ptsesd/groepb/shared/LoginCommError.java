/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared;

/**
 *
 * @author user
 */
public class LoginCommError extends GamePacket {
    
    private String opvulling; 
    public LoginCommError(String header){
        super(header);
        opvulling = "boe";
    }

    public String getOpvulling() {
        return opvulling;
    }

    public void setOpvulling(String opvulling) {
        this.opvulling = opvulling;
    }
    
    
}

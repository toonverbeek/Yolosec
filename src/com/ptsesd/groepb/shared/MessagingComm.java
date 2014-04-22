/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared;

/**
 *
 * @author Tim
 */
public class MessagingComm {
    
    private int spaceShipId;
    private String message;
    private String username;

    public MessagingComm(int spaceShipId, String message, String username) {
        this.spaceShipId = spaceShipId;
        this.message = message;
        this.username = username;
    }

    public int getSpaceShipId() {
        return spaceShipId;
    }

    public void setSpaceShipId(int spaceShipId) {
        this.spaceShipId = spaceShipId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    

    
}

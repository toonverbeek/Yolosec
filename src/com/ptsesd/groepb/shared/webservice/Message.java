/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared.webservice;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tim
 */
@XmlRootElement
public class Message implements Serializable{
    private String message;
    private int spaceshipId;
    private String username;

    public Message(){
        
    }
    
    public Message(String message, int spaceshipId, String username) {
        this.message = message;
        this.spaceshipId = spaceshipId;
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSpaceshipId() {
        return spaceshipId;
    }

    public void setSpaceshipId(int spaceshipId) {
        this.spaceshipId = spaceshipId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
}

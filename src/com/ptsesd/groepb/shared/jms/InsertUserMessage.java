/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared.jms;

/**
 *
 * @author Administrator
 */
public class InsertUserMessage {
    private final long spaceshipId;
    private final String username;
    private final String password;

    public InsertUserMessage(long spaceshipId, String username, String password) {
        this.spaceshipId = spaceshipId;
        this.username = username;
        this.password = password;
    }

    public long getSpaceshipId() {
        return spaceshipId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    
}

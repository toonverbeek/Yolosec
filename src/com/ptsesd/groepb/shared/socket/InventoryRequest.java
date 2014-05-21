/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared.socket;

import com.ptsesd.groepb.shared.GamePacket;

/**
 *
 * @author user
 */
public class InventoryRequest extends GamePacket {
    
    private final long spaceshipId;
    
    public InventoryRequest(String header, long spaceshipId){
        super(header);
        this.spaceshipId = spaceshipId;
    }

    public long getSpaceshipId() {
        return spaceshipId;
    }
}

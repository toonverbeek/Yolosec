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
public class ResourceMessage {
    private final long userId;
    private final int resource_normal;
    private final int resource_magic;
    private final int resource_rare;

    public ResourceMessage(long userId, int resource_normal, int resource_magic, int resource_rare) {
        this.userId = userId;
        this.resource_normal = resource_normal;
        this.resource_magic = resource_magic;
        this.resource_rare = resource_rare;
    }

    public long getUserId() {
        return userId;
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
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared.jms;

import com.google.gson.Gson;
import com.ptsesd.groepb.shared.ItemComm;

/**
 *
 * @author Tim
 */
public class ItemSerializer {
    
    private Gson gson;
    
    /*
     Serializes an ItemComm object to a json string and back for JMS messaging
    */
    public ItemSerializer() {
        gson = new Gson();
    }
    
    public String itemToJson(ItemComm item) {
        String json = gson.toJson(item);
        return json;
    }
    
    public ItemComm jsonToItem(String json) {
        ItemComm item = (ItemComm) gson.fromJson(json, ItemComm.class);
        return item;
    }
    
}

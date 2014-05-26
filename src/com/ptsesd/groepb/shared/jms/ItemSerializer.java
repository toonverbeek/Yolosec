/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared.jms;

import com.google.gson.Gson;
import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.socket.InventoryReply;
import com.ptsesd.groepb.shared.socket.InventoryRequest;

/**
 *
 * @author Tim
 */
public class ItemSerializer {
    
    private static Gson gson = new Gson();
        
    public static String itemToJson(ItemComm item) {
        String json = gson.toJson(item);
        return json;
    }
    
    public static ItemComm jsonToItem(String json) {
        ItemComm item = (ItemComm) gson.fromJson(json, ItemComm.class);
        return item;
    }
    
    public static String inventoryRequestToJson(InventoryRequest invRequest){
        String json = gson.toJson(invRequest);
        return json;
    }
    
    public static InventoryRequest jsonToInventoryRequest(String json){
        InventoryRequest invRequest = (InventoryRequest) gson.fromJson(json, InventoryRequest.class);
        return invRequest;
    }
    
    public static String inventoryReplyToJson(InventoryReply invReply){
        String json = gson.toJson(invReply);
        return json;
    }
    
    public static InventoryReply jsonToInventoryReply(String json){
        InventoryReply invReply = (InventoryReply) gson.fromJson(json, InventoryReply.class);
        return invReply;
    }
    
    public static String resourceMessageToJson(ResourceMessage message){
        String json = gson.toJson(message);
        return json;
    }
    
    public static ResourceMessage jsonToResourceMessage(String json){
        ResourceMessage resMessage = (ResourceMessage) gson.fromJson(json, ResourceMessage.class);
        return resMessage;
    }
}

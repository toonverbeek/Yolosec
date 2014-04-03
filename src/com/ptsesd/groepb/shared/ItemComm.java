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
public class ItemComm {
    private int itemId;
    private int userId;
    private boolean isBuying;
    public ItemComm(int itemId, int userId, boolean isBuying) {
        this.itemId = itemId;
        this.userId = userId;
        this.isBuying = isBuying;
    }

    public int getItemId() {
        return itemId;
    }

    public int getUserId() {
        return userId;
    }

    public boolean getIsBuying() {
        return isBuying;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yolosec.spaceclient.jms;

import com.yolosec.auctionhouse.gui.AuctionHouseFrame;

/**
 *
 * @author Tim
 */
public class TradeUtil {
    
    public static void LaunchAuctionHouse() {
        AuctionHouseFrame aFrame = new AuctionHouseFrame();
        aFrame.setVisible(true);
    }
    
}

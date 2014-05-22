/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.communication;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tim
 */
public class SocketShutdownHook extends Thread {

    public SocketShutdownHook() {

    }

    @Override
    public void run() {
        Communicator.writer.close();
        System.out.println("Socket closed");
    }
}

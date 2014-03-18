/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.observing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.newdawn.slick.util.Log;

/**
 *
 * @author Toon
 * @param <T>
 */
public class NodeImpl<T extends Node> implements Node<T> {

    private final Collection<T> others = Collections.synchronizedList(new ArrayList<T>());

    @Override
    public void subscribe(T other) {
        others.add(other);
    }

    @Override
    public void unsubscribe(T other) {
        others.remove(other);
    }

    @Override
    public void onMessage(String message, Object o) {
        Log.info("Recieved message: " + message);
    }

    @Override
    public void notifyAll(String message, Object o) {
        for (T t : others) {
            t.onMessage(message, o);
        }
    }

    @Override
    public Collection<T> getOthers() {
        return others;
    }

}

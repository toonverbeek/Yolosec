/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.observing;

import java.util.Collection;

/**
 *
 * @author Toon
 * @param <T>
 */
public interface Node<T extends Node> {

    void subscribe(T other);

    void unsubscribe(T other);

    void onMessage(String message, Object o);

    void notifyAll(String message, Object o);

    Collection<T> getOthers();
}

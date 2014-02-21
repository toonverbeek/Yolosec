/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Item;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Lisanne
 */
public interface SpaceshipDAO {

    boolean addItem(Item item);

    Collection<Item> findAll();

    Collection<Item> findItem(String name);

    Item findItem(long id);
    
    boolean removeItem(Item item);
    
    long getCount();
}

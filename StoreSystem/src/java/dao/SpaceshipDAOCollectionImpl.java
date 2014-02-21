/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Item;
import domain.Spaceship;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Lisanne
 */
public class SpaceshipDAOCollectionImpl implements SpaceshipDAO {

    private Spaceship spaceship;

    public SpaceshipDAOCollectionImpl(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    /**
     * Add an item to the spaceship inventory
     * @param item the item to add
     * @return 
     */
    @Override
    public boolean addItem(Item item) {
        return spaceship.addItemToInventory(item);
    }

    /**
     * Get all the items from the inventory
     * @return 
     */
    @Override
    public Collection<Item> findAll() {
        return spaceship.getInventory();
    }

    /**
     * Get items from the inventory with <name>
     * @param name item name
     * @return 
     */
    @Override
    public Collection<Item> findItem(String name) {
        Collection<Item> items = new ArrayList<>();
        if (!name.isEmpty()) {
            for (Item i : spaceship.getInventory()) {
                if (i.getName().equals(name)) {
                    items.add(i);
                }
            }
        }
        return items;
    }

    /**
     * Get item with <id>
     * @param id the id of the item
     * @return 
     */
    @Override
    public Item findItem(long id) {
        for (Item i : spaceship.getInventory()) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    /**
     * Remove an item from the inventory
     * @param item the item to remove
     * @return 
     */
    @Override
    public boolean removeItem(Item item) {
        if(item != null){
            if(spaceship.getInventory().contains(item)){
                return spaceship.getInventory().remove(item);
            }
        }
        return false;
    }

    /**
     * Get the amount of items in the inventory
     * @return 
     */
    @Override
    public long getCount() {
        return this.spaceship.getInventory().size();
    }

}

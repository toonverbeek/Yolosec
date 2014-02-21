/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Collection;

public class Spaceship {

    private Collection<Item> inventory;

    public Spaceship() {
    }

    /**
     * Get the inventory of the spaceship
     *
     * @return
     */
    public Collection<Item> getInventory() {
        return inventory;
    }

    /**
     * Set the inventory of the spaceship
     *
     * @param inventory
     */
    public void setInventory(Collection<Item> inventory) {
        this.inventory = inventory;
    }
    
    /**
     * Add an item to the inventory
     * @param item the item to add
     * @return 
     */
    public boolean addItemToInventory(Item item){
        if(item != null){
            return this.inventory.add(item);
        }
        return false;
    }
}

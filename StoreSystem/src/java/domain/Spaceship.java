/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Spaceship implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name="hdksaldhalk";
    
    @ManyToMany
    private Collection<Item> inventory;

    public Spaceship() {
    }
    public Spaceship(Collection<Item> inventory) {
        this.inventory = inventory;
    }

    /**
     * Get items from the inventory with <name>
     *
     * @param name item name
     * @return
     */
    public Collection<Item> findItem(String name) {
        Collection<Item> items = new ArrayList<>();
        if (!name.isEmpty()) {
            for (Item i : inventory) {
                if (i.getName().equals(name)) {
                    items.add(i);
                }
            }
        }
        return items;
    }

    /**
     * Get item with <id>
     *
     * @param id the id of the item
     * @return
     */
    public Item findItem(long id) {
        for (Item i : inventory) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    /**
     * Remove an item from the inventory
     *
     * @param item the item to remove
     * @return
     */
    public boolean removeItem(Item item) {
        if (item != null) {
            if (inventory.contains(item)) {
                return inventory.remove(item);
            }
        }
        return false;
    }

    /**
     * Get all the items from the inventory
     *
     * @return
     */
    public Collection<Item> getAllItems() {
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
     *
     * @param item the item to add
     * @return
     */
    public boolean addItemToInventory(Item item) {
        if (item != null) {
            return this.inventory.add(item);
        }
        return false;
    }

    /**
     * Get the amount of items in the inventory
     *
     * @return
     */
    public long getItemCount() {
        return this.inventory.size();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

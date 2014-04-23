/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Jamy
 */
@Entity
public class AuctionHouseItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Item item;

    private String asteroidType;
    private int resourceAmount;
    private int sellerId;

    public AuctionHouseItem(Item item, int sellerId, int resourceAmount, String asteroidType) {
        this.item = item;
        this.sellerId = sellerId;
        this.resourceAmount = resourceAmount;
        this.asteroidType = asteroidType;
    }

    public AuctionHouseItem() {
    }

    public Item getItem() {
        return item;
    }

    public String getAsteroidType() {
        return asteroidType;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }

    public int getUserId() {
        return sellerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

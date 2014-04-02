/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
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

    @OneToOne
    private Item item;

    private String asteroidType;
    private int resourceAmount;
    private int userId;

    public AuctionHouseItem(Item item, int userId, int resourceAmount, String asteroidType) {
        this.item = item;
        this.userId = userId;
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
        return userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

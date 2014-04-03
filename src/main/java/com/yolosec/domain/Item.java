package com.yolosec.domain;

/**
 *
 * @author Administrator
 */
public class Item {
    
    private String name;
    private String description;
    private Double price;
    private Long id;

    public Item(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }
}

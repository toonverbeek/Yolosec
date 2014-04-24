package com.yolosec.domain;

/**
 *
 * @author Administrator
 */
public class Spaceship {

    private Integer direction;
    private Integer position_x;
    private Integer position_y;
    private Integer resource_common;
    private Integer resource_rare;
    private Integer resource_magic;

    public Spaceship() {
        this.direction = 1;
        this.position_x = 0;
        this.position_y = 0;
        this.resource_common = 0;
        this.resource_rare = 0;
        this.resource_magic = 0;
    }

    public Spaceship(Integer direction, Integer position_x, Integer position_y) {
        this.direction = direction;
        this.position_x = position_x;
        this.position_y = position_y;
        this.resource_common = 0;
        this.resource_rare = 0;
        this.resource_magic = 0;
    }

    public Spaceship(Integer direction, Integer position_x, Integer position_y, Integer resource_common, Integer resource_rare, Integer resource_magic) {
        this.direction = direction;
        this.position_x = position_x;
        this.position_y = position_y;
        this.resource_common = resource_common;
        this.resource_rare = resource_rare;
        this.resource_magic = resource_magic;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getPosition_x() {
        return position_x;
    }

    public void setPosition_x(Integer position_x) {
        this.position_x = position_x;
    }

    public Integer getPosition_y() {
        return position_y;
    }

    public void setPosition_y(Integer position_y) {
        this.position_y = position_y;
    }

    public Integer getResource_common() {
        return resource_common;
    }

    public void setResource_common(Integer resource_common) {
        this.resource_common = resource_common;
    }

    public Integer getResource_rare() {
        return resource_rare;
    }

    public void setResource_rare(Integer resource_rare) {
        this.resource_rare = resource_rare;
    }

    public Integer getResource_magic() {
        return resource_magic;
    }

    public void setResource_magic(Integer resource_magic) {
        this.resource_magic = resource_magic;
    }
}

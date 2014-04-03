package com.yolosec.domain;

/**
 *
 * @author user
 */
public class Position {

    private int posX1, posY1;
    private int size;

    public Position(int posX1, int posY1, int size) {
        this.posX1 = posX1;
        this.posY1 = posY1;
        this.size = size;
    }

    public int getPosX1() {
        return posX1;
    }

    public int getPosY1() {
        return posY1;
    }

    public int getSize() {
        return size;
    }
}

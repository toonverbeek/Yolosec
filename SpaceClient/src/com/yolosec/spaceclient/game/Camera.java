package com.yolosec.spaceclient.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;
import com.yolosec.spaceclient.gui.SpaceClient;

public class Camera {

    private int x, y;
    private int mapWidth, mapHeight;
    private Rectangle viewPort;

    public Camera(TiledMap map, int mapWidth, int mapHeight) {
        x = 0;
        y = 0;
        viewPort = new Rectangle(0, 0, SpaceClient.screenWidth, SpaceClient.screenHeight);
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public void translate(Graphics g, Spaceship spaceship) {

        if (spaceship.getPosition().x - SpaceClient.screenWidth / 2 + 16 < 0) {
            x = 0;
        } else if (spaceship.getPosition().x + SpaceClient.screenWidth / 2 + 16 > mapWidth) {
            x = -mapWidth + SpaceClient.screenWidth;
        } else {
            x = (int) -spaceship.getPosition().x + SpaceClient.screenWidth / 2 - 16;
        }

        if (spaceship.getPosition().y - SpaceClient.screenHeight / 2 + 16 < 0) {
            y = 0;
        } else if (spaceship.getPosition().y + SpaceClient.screenHeight / 2 + 16 > mapHeight) {
            y = -mapHeight + SpaceClient.screenHeight;
        } else {
            y = (int) -spaceship.getPosition().y + SpaceClient.screenHeight / 2 - 16;
        }
        g.translate(x, y);
        viewPort.setX(-x);
        viewPort.setY(-y);
    }
}
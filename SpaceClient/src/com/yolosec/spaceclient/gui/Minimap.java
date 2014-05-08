/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.gui;

import com.ptsesd.groepb.shared.AsteroidType;
import com.yolosec.spaceclient.dao.interfaces.GameObject;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.game.world.Asteroid;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
import com.yolosec.spaceclient.game.world.Viewport;
import java.util.List;
import org.newdawn.slick.*;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.*;

/**
 *
 * @author Lisanne
 */
public class Minimap extends GameObjectImpl {

    private Shape map;
    private Vector2f mapPosition;
    private float width;
    private float height;
    private float scaleNumber;

    public Minimap(int x, int y, int width, int height) {
        mapPosition = new Vector2f(x, y);
        map = new Rectangle(x, y, width, height);
        this.width = width;
        this.height = height;
        scaleNumber = (Viewport.TILESIZE * Viewport.tilemapWidth) / width;
    }

    @Override
    public java.awt.Rectangle getRectangle() {
        return new java.awt.Rectangle();
    }

    public void render(Graphics g, Spaceship self, List<GameObjectImpl> gameObjects) {
        //draw the base map
        g.setColor(Color.orange);
        g.fill(map, new GradientFill(mapPosition.x, mapPosition.y, Color.black, mapPosition.x + width, mapPosition.y + height, Color.black, true));
        g.draw(map);
        //draq the game objects
        for (GameObject gObject : gameObjects) {
            if (gObject instanceof Asteroid) {
                Asteroid ast = (Asteroid) gObject;
                float astxPosition = ast.getXPosition();
                float astyPosition = ast.getYPosition();
                AsteroidType type = ast.getType();
                switch (type) {
                    case common:
                        g.setColor(Color.red);
                        break;
                    case magic:
                        g.setColor(Color.yellow);
                        break;
                    case rare:
                        g.setColor(Color.pink);
                        break;
                }
                float astmapxPosition = astxPosition / scaleNumber + mapPosition.x;
                float astmapyPosition = astyPosition / scaleNumber + mapPosition.y;
                float wh = ast.getResourceAmount() / scaleNumber;
                Shape asteroid = new Rectangle(astmapxPosition, astmapyPosition, wh, wh);
                g.draw(asteroid);
            } else if (gObject instanceof Spaceship) {
                Spaceship spaceship2 = (Spaceship) gObject;
                float spacexPosition = spaceship2.getPosition().x;
                float spaceyPosition = spaceship2.getPosition().y;
                g.setColor(Color.white);
                float spacemapxPosition = spacexPosition / scaleNumber + mapPosition.x;
                float spacemapyPosition = spaceyPosition / scaleNumber + mapPosition.y;
                Shape spaceship = new Rectangle(spacemapxPosition, spacemapyPosition, 1, 1);
                g.draw(spaceship);
            }
        }
        
        float ownSpaceshipxPosition = self.getPosition().x;
        float ownSpaceshipyPosition = self.getPosition().y;
        g.setColor(Color.cyan);
        float spacemapxPosition = ownSpaceshipxPosition / scaleNumber + mapPosition.x;
        float spacemapyPosition = ownSpaceshipyPosition / scaleNumber + mapPosition.y;
        Shape spaceship = new Rectangle(spacemapxPosition, spacemapyPosition, 1, 1);
        g.draw(spaceship);
        
        g.setColor(Color.cyan);
        float ownviewportxPosition = Viewport.viewportPos.x / scaleNumber + mapPosition.x;
        float ownviewportyPosition = Viewport.viewportPos.y / scaleNumber + mapPosition.y;
        float vwidth = SpaceClient.screenWidth / scaleNumber;
        float vheight = SpaceClient.screenHeight / scaleNumber;
        Shape viewport = new Rectangle(ownviewportxPosition, ownviewportyPosition, vwidth, vheight);
        g.draw(viewport);
    }
}

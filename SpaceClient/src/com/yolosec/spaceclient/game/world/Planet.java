/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game.world;

import com.ptsesd.groepb.shared.AsteroidType;
import com.ptsesd.groepb.shared.PlanetComm;
import com.yolosec.spaceclient.dao.interfaces.DrawableComponent;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.gui.SpaceClient;
import java.awt.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Tim
 */
public class Planet extends GameObjectImpl implements DrawableComponent {

    private Rectangle planetBounding;
    private int size;
    private float x, y;
    private String name;

    public Planet(int size, float x, float y, String name) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.planetBounding = new Rectangle((int) x, (int) y, size, size);
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getSize() {
        return this.size;
    }

    @Override
    public Rectangle getRectangle() {
        return planetBounding;
    }

    @Override
    public void update(GameContainer gc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(Graphics g, boolean self) {
        int drawPositionX = (int) (x - Viewport.viewportPos.x);
        int drawPositionY = (int) (y - Viewport.viewportPos.y);
        g.setAntiAlias(true);
        g.setColor(Color.white);
        g.setColor(new Color(0, 168, 14));
        g.setLineWidth(4);
        g.drawGradientLine(drawPositionX, drawPositionY, Color.yellow, drawPositionX + size, drawPositionY, Color.black);
        g.drawGradientLine(drawPositionX + size, drawPositionY, Color.black, drawPositionX + size, drawPositionY + size, Color.yellow);
        g.drawGradientLine(drawPositionX + size, drawPositionY + size, Color.yellow, drawPositionX, drawPositionY + size, Color.black);
        g.drawGradientLine(drawPositionX, drawPositionY + size, Color.black, drawPositionX, drawPositionY, Color.yellow);
        g.setLineWidth(2);
        g.drawOval(drawPositionX, drawPositionY, planetBounding.width, planetBounding.width);
        g.setLineWidth(1);
        g.setAntiAlias(false);
        g.setColor(Color.white);
        g.drawString("TEseztzstadfgkasdflj", x, y);
    }

}

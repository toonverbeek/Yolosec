/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import com.yolosec.spaceclient.dao.interfaces.DrawableComponent;
import com.yolosec.spaceclient.game.Spaceship;

/**
 *
 * @author Toon
 */
public class User implements DrawableComponent {

    private Spaceship spaceship;
    private String username;

    public User(Spaceship spaceship, String username) {
        this.spaceship = spaceship;
        this.username = username;
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void update(GameContainer gc) {
        this.spaceship.update(gc);
    }

    @Override
    public void render(Graphics g, boolean self) {
        this.spaceship.render(g, self);
    }
}

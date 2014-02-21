/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import spaceclient.dao.interfaces.DrawableComponent;
import spaceclient.game.Spaceship;

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
    public void render(Graphics g) {
        this.spaceship.render(g);
    }
}

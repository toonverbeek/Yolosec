/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceclient.dao.interfaces;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Toon
 */
public interface DrawableComponent {
    void update(GameContainer gc);
    
    void render(Graphics g, boolean self);
}

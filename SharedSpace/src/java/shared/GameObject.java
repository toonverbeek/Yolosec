/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.awt.Rectangle;

/**
 *
 * @author Toon
 */
public abstract class GameObject  {
    public boolean intersects(GameObject object) {
        return this.getRectangle().intersects(object.getRectangle());
    }
    public abstract Rectangle getRectangle();
}

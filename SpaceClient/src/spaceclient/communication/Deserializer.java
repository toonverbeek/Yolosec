/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.communication;

import org.newdawn.slick.geom.Vector2f;
import shared.AsteroidComm;
import shared.SpaceshipComm;
import spaceclient.game.Asteroid;
import spaceclient.game.Spaceship;

/**
 *
 * @author Toon
 */
public class Deserializer {

    public static Spaceship deserializeSpaceship(SpaceshipComm scomm) {
        Spaceship sShip = new Spaceship(10, 10);
        sShip.setPosition(new Vector2f(scomm.getX(), scomm.getY()));
        sShip.setDirection(scomm.getDirection());
        return sShip;
    }

    public static Asteroid deserializeAsAsteroid(AsteroidComm acom) {
        return new Asteroid(acom);
    }
}

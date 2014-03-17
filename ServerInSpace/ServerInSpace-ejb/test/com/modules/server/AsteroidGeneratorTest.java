
package com.modules.server;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shared.Asteroid;

/**
 *
 * @author user
 */
public class AsteroidGeneratorTest {
    
    public AsteroidGeneratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void createAsteroids(){
        ServerAsteroidGenerator sag = new ServerAsteroidGenerator();
        
        //Generate a list of asteroids
        sag.generateAsteroids();
        List<Asteroid> asteroids = sag.getAsteroids();
        
        for(Asteroid a : asteroids){
            System.out.println(String.format("asteroid - resource amount {%s} || pos X {%s} || pos Y {%s} || type {%s}", a.getResourceAmount(), a.getX(), a.getY(), a.getType()));
        }
        assertEquals(asteroids.size(), 25);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}

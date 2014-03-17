
package com.modules.server;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author user
 */
public class AsteroidGeneratorPositionTest {
    
    public AsteroidGeneratorPositionTest() {
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
    public void CheckPositionCollisionEqual(){
        ServerAsteroidGenerator sag = new ServerAsteroidGenerator();
        
        //if on the exact same position
        //start testcase
        position p1 = new position(20, 20, 10);
        position p2 = new position(20, 20, 10);
        
        boolean colP12 = sag.positionsColliding(p1, p2);
        assertTrue(colP12);
        //end test case
    }
    
    @Test
    public void CheckPositionCollisionLeft(){
        ServerAsteroidGenerator sag = new ServerAsteroidGenerator();
        
        //if p1 is left from p2, with collision
        //start test case
        position p1 = new position(20, 20, 10);
        position p2 = new position(15, 20, 10);
        
        boolean colP12 = sag.positionsColliding(p1, p2);
        assertTrue(colP12);
        //end test case
        
        //if p3 is left from p4, without collision
        //start test case
        position p3 = new position(20, 20, 10);
        position p4 = new position(9, 20, 10);
        
        boolean colP34 = sag.positionsColliding(p3, p4);
        assertFalse(colP34);
        //end test case
    }
    
    @Test
    public void CheckPositionCollisionRight(){
        ServerAsteroidGenerator sag = new ServerAsteroidGenerator();
        //if p1 is right from p2, without collision
        //start test case
        position p1 = new position(10, 10, 10);
        position p2 = new position(21, 10, 10);
        
        boolean colP12 = sag.positionsColliding(p1, p2);
        assertFalse(colP12);
        //end test case
        
        //if p4 is right from p3, with collision
        //start test case
        position p3 = new position(10, 10, 10);
        position p4 = new position(15, 10, 10);
        
        boolean colp34 = sag.positionsColliding(p3, p4);    
        assertTrue(colp34);
        
        //if x is the same but on different y, without collision
        //start test case
        position p5 = new position(10, 10, 10);
        position p6 = new position(10, 25, 10);
        
        boolean colp56 = sag.positionsColliding(p5, p6);
        assertFalse(colp56);
        //end test case
        
        //if x is the same but on differnt y, with collision
        //start test case
        position p7 = new position(10, 10, 10);
        position p8 = new position(10, 19, 10);
        
        boolean colp78 = sag.positionsColliding(p7, p8);
        assertTrue(colp78);
        //end test case
    }
    
    @Test
    public void checkPositionOverlapCollision(){
        ServerAsteroidGenerator sag = new ServerAsteroidGenerator();
        //if p5 is inside p6
        //start test case
        position p5 = new position(20, 20, 10);
        position p6 = new position(10, 10, 40);
        
        boolean colP56 = sag.positionsColliding(p5, p6);
        assertTrue(colP56);
        //end test case
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}

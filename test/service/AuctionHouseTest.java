/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.ptsesd.groepb.shared.AsteroidType;
import domain.AuctionHouseItem;
import domain.Item;
import domain.Resource;
import domain.Stat;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jamy
 */
public class AuctionHouseTest {

    private AuctionHouse ah;

    public AuctionHouseTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        ah = new AuctionHouse();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void postItemToAuctionHouse() {
        List<Resource> item1Resources = new ArrayList<>();
        item1Resources.add(new Resource("Common", 100));
        item1Resources.add(new Resource("Magic", 50));
        List<Stat> item1Stats = new ArrayList<>();
        item1Stats.add(new Stat("Stamina", 10));
        String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis hendrerit velit sit amet ligula faucibus, a aliquam dui consequat. Sed egestas mauris gravida sem commodo tristique. Maecenas sed mauris metus. Vestibulum nunc nunc, pretium et interdum eget, tincidunt ut eros. Suspendisse in urna nec enim cursus fermentum. Quisque ultricies eros laoreet, molestie odio vitae, congue nisl. Praesent eget eleifend lectus.";

        Item item = new Item("Item 1", lorem, item1Resources, item1Stats, "");
        int userId = 1;

        String asteroidType = "Common";
        int resourceAmount = 100;

        AuctionHouseItem ahItem = new AuctionHouseItem(item, userId, resourceAmount, asteroidType);

        //Persist to database
        boolean result = ah.addItem(ahItem);
        assertEquals(true, result);

        long ahItemId = ahItem.getId();
        AuctionHouseItem dbItem = ah.getAuctionHouseItem(ahItemId);
        assertEquals(ahItem, dbItem);

        resourceAmount = -100;
        ahItem = new AuctionHouseItem(item, userId, resourceAmount, asteroidType);
        assertEquals(false, ah.addItem(ahItem));

//        ahItemId = ahItem.getId();
//        dbItem = ah.getAuctionHouseItem(ahItemId);
//        assertEquals(ahItem, dbItem);
    }

    @Test
    public void boughtItemFromAuctionHouse() {
        int ahItemId = 1;

        AuctionHouseItem item = ah.buyItem(ahItemId);
        assertNotNull(item);

        AuctionHouseItem dbItem = ah.getAuctionHouseItem(ahItemId);
        assertNull(dbItem);
    }
}

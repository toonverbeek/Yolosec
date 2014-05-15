/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.ptsesd.groepb.shared.AuctionHouseRequestType;
import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.jms.ItemSerializer;
import com.ptsesd.groepb.shared.jms.MessagingGateway;
import dao.ItemDAO;
import dao.ItemDAO_JPAImpl;
import domain.AuctionHouseItem;
import domain.Item;
import domain.Resource;
import domain.Stat;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import jms.GameserverGateway;
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
    private final ItemDAO itemDAO = new ItemDAO_JPAImpl();
    List<Resource> item1Resources;
    List<Stat> item1Stats;
    String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis hendrerit velit sit amet ligula faucibus, a aliquam dui consequat. Sed egestas mauris gravida sem commodo tristique. Maecenas sed mauris metus. Vestibulum nunc nunc, pretium et interdum eget, tincidunt ut eros. Suspendisse in urna nec enim cursus fermentum. Quisque ultricies eros laoreet, molestie odio vitae, congue nisl. Praesent eget eleifend lectus.";
    private Gson gson = new Gson();

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
        item1Resources = new ArrayList<>();
        item1Resources.add(new Resource("Common", 100));
        item1Resources.add(new Resource("Magic", 50));
        item1Stats = new ArrayList<>();
        item1Stats.add(new Stat("Stamina", 10));
        Item item = new Item("Item 1", "Description 1", item1Resources, item1Stats, "");

        AuctionHouseItem auctionHouseItem = new AuctionHouseItem(item, 1, 100, "Common");
        ah.putItemForSale(auctionHouseItem);

    }

    @After
    public void tearDown() {
    }

    /**
     * Tests adding an item to the AuctionHouse.
     */
    @Test
    public void postItemToAuctionHouse() {
        //Init
        List<Resource> itemForSaleResoures = new ArrayList<>();
        itemForSaleResoures.add(new Resource("Common", 100));
        itemForSaleResoures.add(new Resource("Magic", 50));
        List<Stat> itemForSaleStats = new ArrayList<>();
        itemForSaleStats.add(new Stat("Stamina", 10));

        Item itemForSale = new Item("Item FOR SALE", "ITEM FOR SALE", itemForSaleResoures, itemForSaleStats, "");
        int userId = 1;

        String asteroidType = "Common";
        int resourceAmount = 100;

        //New Auctionhouse Item
        AuctionHouseItem ahItem = new AuctionHouseItem(itemForSale, userId, resourceAmount, asteroidType);

        //Persist AunctionhouseItem to database
        boolean result = ah.putItemForSale(ahItem);
        assertEquals(true, result);

        //Check if the item exists in the database
        long ahItemId = ahItem.getId();
        AuctionHouseItem dbItem = ah.getAuctionHouseItem(ahItemId);
        assertEquals(ahItem, dbItem);

        //Make sure that we cannot put up an item with a negative resource amount
        resourceAmount = -100;
        ahItem = new AuctionHouseItem(itemForSale, userId, resourceAmount, asteroidType);
        assertEquals(false, ah.putItemForSale(ahItem));
    }

    /**
     * Tests buying an item from the AuctionHouse.
     */
    @Test
    public void boughtItemFromAuctionHouse() {
        int ahItemId = 1;

        //Check if we successfully can retrieve an an item
        //Buys an item, should be removed from the database afterwards
        AuctionHouseItem item = ah.buyItem(ahItemId);
        assertNotNull(item);

        //Check if the item is succesfully removed
        AuctionHouseItem dbItem = ah.getAuctionHouseItem(ahItemId);
        assertNull(dbItem);
    }

    @Test
    public void newItemForSaleRequest() {
       
    }
}

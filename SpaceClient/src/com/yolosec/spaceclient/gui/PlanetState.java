/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.gui;

import com.ptsesd.groepb.shared.AuctionHouseRequestType;
import com.ptsesd.groepb.shared.socket.InventoryRequest;
import com.yolosec.spaceclient.communication.BroadcastHandler;
import com.yolosec.spaceclient.dao.interfaces.DrawCallback;
import com.yolosec.spaceclient.game.player.Inventory;
import com.yolosec.spaceclient.game.player.Item;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Toon
 */
public class PlanetState extends BasicGameState implements DrawCallback {

    //---------------------------------------GUI LAYOUT ELEMENTS----------------------------------------------------
    private static final int borderMargin = 20;

    private Shape buyButton;
    private Shape sellButton;
    private Shape exitPlanet;
    private Shape buttonsBorder;

    private Shape[] inventorySlots = new Shape[12];
    private Shape baseSlot;
    private static int SLOTWIDTH, SLOTHEIGHT;
    private Shape inventorySlotsBorder;

    private Shape auctionhouseBorder;
    private int spaceshipid;
    private boolean buyView;
    private boolean sellView;

    private Inventory inventory;
    private Inventory auctionhouseInventory;
    private boolean inventorycalled = false;

    private Shape auctionhouseItemBase;
    private ArrayList<Shape> auctionhouseItemBorders;

    //start counting from one!!
    private static double ITEMSPERPAGE = 3;
    private int totalPages;
    private int currentPage = 0;
    private Shape auctionhousePageBase;
    private ArrayList<Shape> auctionhousePageBorders;

    private ArrayList<Item> sellItems;
    private ArrayList<Item> buyItems;
    private ArrayList<Item> cancelItems;

    private Shape auctionhouseButtonBase;
    private ArrayList<Shape> sellButtons;
    private ArrayList<Shape> buyButtons;
    private BroadcastHandler handler;

    //private static final Rec
    //---------------------------------------------------------------------------------------------------------------
    public PlanetState(int spaceshipId) {
        this.spaceshipid = spaceshipId;
    }

    @Override
    public int getID() {
        return 2;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        int inventoryRectX = borderMargin, inventoryRectY = borderMargin;
        int navigationRectX = borderMargin;
        int mainRectY = borderMargin;

        int oneThirdOfScreenWidth = (container.getScreenWidth() / 3);
        int oneThirdOfScreenHeight = (container.getScreenHeight() / 3);

        //draw the inventory on 2/3 of the y-axis and on 1/3 of the x-axis
        int inventoryRectWidth = oneThirdOfScreenWidth - borderMargin;
        int inventoryRectHeight = (oneThirdOfScreenHeight * 2) - borderMargin;
        SLOTWIDTH = (inventoryRectWidth - (5 * borderMargin)) / 4;
        SLOTHEIGHT = (inventoryRectWidth - (5 * borderMargin)) / 4;

        inventorySlotsBorder = new Rectangle(inventoryRectX, inventoryRectY, inventoryRectWidth, inventoryRectHeight);
        baseSlot = new Rectangle(inventorySlotsBorder.getX() + borderMargin, inventorySlotsBorder.getY() + borderMargin * 2, SLOTWIDTH, SLOTHEIGHT);
        //the first row
        inventorySlots[0] = baseSlot;
        inventorySlots[1] = new Rectangle(inventorySlots[0].getX() + SLOTWIDTH + borderMargin, inventorySlots[0].getY(), inventorySlots[0].getWidth(), inventorySlots[0].getHeight());
        inventorySlots[2] = new Rectangle(inventorySlots[1].getX() + SLOTWIDTH + borderMargin, inventorySlots[1].getY(), inventorySlots[1].getWidth(), inventorySlots[1].getHeight());
        inventorySlots[3] = new Rectangle(inventorySlots[2].getX() + SLOTWIDTH + borderMargin, inventorySlots[2].getY(), inventorySlots[2].getWidth(), inventorySlots[2].getHeight());

        //the second row
        inventorySlots[4] = new Rectangle(inventorySlots[0].getX(), inventorySlots[0].getY() + SLOTHEIGHT + borderMargin, inventorySlots[0].getWidth(), inventorySlots[0].getHeight());
        inventorySlots[5] = new Rectangle(inventorySlots[4].getX() + SLOTWIDTH + borderMargin, inventorySlots[4].getY(), inventorySlots[4].getWidth(), inventorySlots[4].getHeight());
        inventorySlots[6] = new Rectangle(inventorySlots[5].getX() + SLOTWIDTH + borderMargin, inventorySlots[5].getY(), inventorySlots[5].getWidth(), inventorySlots[5].getHeight());
        inventorySlots[7] = new Rectangle(inventorySlots[6].getX() + SLOTWIDTH + borderMargin, inventorySlots[6].getY(), inventorySlots[6].getWidth(), inventorySlots[6].getHeight());

        //the third row
        inventorySlots[8] = new Rectangle(inventorySlots[4].getX(), inventorySlots[4].getY() + SLOTHEIGHT + borderMargin, inventorySlots[4].getWidth(), inventorySlots[4].getHeight());
        inventorySlots[9] = new Rectangle(inventorySlots[8].getX() + SLOTWIDTH + borderMargin, inventorySlots[8].getY(), inventorySlots[8].getWidth(), inventorySlots[8].getHeight());
        inventorySlots[10] = new Rectangle(inventorySlots[9].getX() + SLOTWIDTH + borderMargin, inventorySlots[9].getY(), inventorySlots[9].getWidth(), inventorySlots[9].getHeight());
        inventorySlots[11] = new Rectangle(inventorySlots[10].getX() + SLOTWIDTH + borderMargin, inventorySlots[10].getY(), inventorySlots[10].getWidth(), inventorySlots[10].getHeight());

        //draw the navigation section on 1/3 of the y-axis and on 1/3 of the x-axis
        int navigationRectY = inventoryRectY + inventoryRectHeight + borderMargin;

        int navigationRectWidth = oneThirdOfScreenWidth - (borderMargin);
        int navigationRectHeight = oneThirdOfScreenHeight - (2 * borderMargin);
        int buttonWidth = navigationRectWidth - 2 * borderMargin;
        int buttonHeight = navigationRectHeight / 7;

        buttonsBorder = new Rectangle(navigationRectX, navigationRectY, navigationRectWidth, navigationRectHeight);
        buyButton = new Rectangle(navigationRectX + borderMargin, navigationRectY + buttonHeight, buttonWidth, buttonHeight);
        sellButton = new Rectangle(buyButton.getX(), buyButton.getY() + 2 * buttonHeight, buttonWidth, buttonHeight);
        exitPlanet = new Rectangle(sellButton.getX(), sellButton.getY() + 2 * buttonHeight, buttonWidth, buttonHeight);

        //x position is right of the first column
        int mainRectX = inventoryRectX + inventoryRectWidth + borderMargin;
        //width is 2/3 of the screen plus margin
        int mainRectWidth = (oneThirdOfScreenWidth * 2) - (2 * borderMargin);
        //height is complete screen plus margin
        int mainRectHeight = container.getScreenHeight() - (2 * borderMargin);

        auctionhouseBorder = new Rectangle(mainRectX, mainRectY, mainRectWidth, mainRectHeight);
        //set the base of an auctionhouseitemborder
        int auctionitemBaseX = (int) (auctionhouseBorder.getX() + borderMargin);
        int auctionitemBaseY = (int) (auctionhouseBorder.getY() + borderMargin);
        int auctionitemBaseWidth = (int) (auctionhouseBorder.getWidth() - (2 * borderMargin));
        int auctionitemBaseHeight = (int) ((auctionhouseBorder.getHeight() - (borderMargin * (ITEMSPERPAGE + 3))) / (ITEMSPERPAGE + 2));
        auctionhouseItemBase = new Rectangle(auctionitemBaseX, auctionitemBaseY, auctionitemBaseWidth, auctionitemBaseHeight);

        int auctionbuttonBaseWidth = (int) (auctionhouseItemBase.getWidth() * 0.25);
        int auctionbuttonBaseHeight = (int) (auctionhouseItemBase.getHeight() - (2 * borderMargin));
        int auctionbuttonBaseX = (int) (auctionhouseItemBase.getX() + auctionitemBaseWidth - borderMargin - auctionbuttonBaseWidth);
        int auctionbuttonBaseY = (int) (auctionhouseItemBase.getY() + borderMargin);
        auctionhouseButtonBase = new Rectangle(auctionbuttonBaseX, auctionbuttonBaseY, auctionbuttonBaseWidth, auctionbuttonBaseHeight);

        int auctionpageBaseWidth = borderMargin;
        int auctionpageBaseHeight = borderMargin;
        int auctionpageBaseX = auctionitemBaseX + borderMargin;
        int auctionpageBaseY = auctionitemBaseY + auctionitemBaseHeight - auctionpageBaseHeight - borderMargin;
        auctionhousePageBase = new Rectangle(auctionpageBaseX, auctionpageBaseY, auctionpageBaseWidth, auctionpageBaseHeight);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        //g.drawRect(inventoryRectX, inventoryRectY, inventoryRectWidth, inventoryRectHeight);

        g.setColor(Color.white);
        //draw the buttons
        g.draw(buttonsBorder);
        g.draw(buyButton);
        g.drawString("buy", buyButton.getX(), buyButton.getY());
        g.draw(sellButton);
        g.drawString("sell", sellButton.getX(), sellButton.getY());
        g.draw(exitPlanet);
        g.drawString("exit planet", exitPlanet.getX(), exitPlanet.getY());

        //draw the inventory
        g.draw(inventorySlotsBorder);
        g.drawString("inventory", inventorySlotsBorder.getX(), inventorySlotsBorder.getY());
        for (Shape s : inventorySlots) {
            g.draw(s);
        }

        g.setColor(Color.white);
        g.draw(auctionhouseBorder);
        g.drawString("auctionhouse", auctionhouseBorder.getX(), auctionhouseBorder.getY());
        drawInventory(g);
        g.draw(auctionhouseItemBase);
        drawCommonEntries(g);
        if (buyView) {
            drawBuyView(g);
        } else if (sellView) {
            drawSellView(g);
        }
    }

    /**
     * Draw the complete inventory
     *
     * @param g
     */
    private void drawInventory(Graphics g) {
        if (inventory != null) {
            int itemslot = 0;
            for (Item item : inventory.getItems()) {
                switch (itemslot) {
                    case 0:
                        drawInventorySlot(g, itemslot, Color.red, item);
                        break;
                    case 1:
                        drawInventorySlot(g, itemslot, Color.yellow, item);
                        break;
                    case 2:
                        drawInventorySlot(g, itemslot, Color.green, item);
                        break;
                    case 3:
                        drawInventorySlot(g, itemslot, Color.white, item);
                        break;
                    case 4:
                        drawInventorySlot(g, itemslot, Color.white, item);
                        break;
                    case 5:
                        drawInventorySlot(g, itemslot, Color.white, item);
                        break;
                    case 6:
                        drawInventorySlot(g, itemslot, Color.white, item);
                        break;
                    case 7:
                        drawInventorySlot(g, itemslot, Color.white, item);
                        break;
                    case 8:
                        drawInventorySlot(g, itemslot, Color.white, item);
                        break;
                    case 9:
                        drawInventorySlot(g, itemslot, Color.white, item);
                        break;
                    case 10:
                        drawInventorySlot(g, itemslot, Color.white, item);
                        break;
                    case 11:
                        drawInventorySlot(g, itemslot, Color.white, item);
                        break;
                }
                itemslot++;
            }
        }
    }

    /**
     * Draw individual inventory slot
     *
     * @param g
     * @param slotid
     * @param color
     * @param item
     */
    private void drawInventorySlot(Graphics g, int slotid, Color color, Item item) {
        g.setColor(color);
        g.draw(new Ellipse(inventorySlots[slotid].getCenterX(), inventorySlots[slotid].getCenterY(), 10, 10));
        g.drawString(item.getResourceAmount() + "", inventorySlots[slotid].getX(), inventorySlots[slotid].getY());
    }

    private void drawCommonEntries(Graphics g) {
        if (auctionhousePageBorders != null) {
            int pagenr = 0;
            for (Shape s : auctionhousePageBorders) {
                if (pagenr == currentPage) {
                    g.setColor(Color.yellow);
                }
                g.draw(s);
                g.drawString((pagenr + 1) + "", s.getX(), s.getY());
                pagenr++;
                g.setColor(Color.white);
            }
        }

        if (auctionhouseItemBorders != null) {
            for (int i = currentPage; i <= (currentPage + ITEMSPERPAGE); i++) {
                if ((i + (currentPage * ITEMSPERPAGE)) < auctionhouseItemBorders.size()) {
                    //System.out.println("SIZE: " + auctionhouseItemBorders.size() + " CURRENT ITEM: " + (i + (currentPage * ITEMSPERPAGE)) + " CURRENT PAGE: " + currentPage + " TILL MAX: " + (currentPage + ITEMSPERPAGE));
                    Shape s = auctionhouseItemBorders.get((int) (i + (currentPage * ITEMSPERPAGE)));//i);
                    g.draw(s);
                }
            }
        }

    }

    /**
     * Draw the sell view
     *
     * @param g
     */
    private void drawSellView(Graphics g) {
        if (sellButtons != null) {
            for (int i = currentPage; i <= currentPage + ITEMSPERPAGE; i++) {
                if ((i + (currentPage * ITEMSPERPAGE)) < sellButtons.size()) {
                    Shape s = sellButtons.get((int) (i + (currentPage * ITEMSPERPAGE)));//i);
                    g.draw(s);
                }
            }
        }

        if (sellItems != null
                && sellButtons != null
                && auctionhouseItemBorders != null) {
            boolean isCancelDone = false;

            for (int i = currentPage; i <= currentPage + ITEMSPERPAGE; i++) {
                if ((i + (currentPage * ITEMSPERPAGE)) < cancelItems.size()) {
                    Item item = cancelItems.get((int) (i + (currentPage * ITEMSPERPAGE)));
                    Shape btn = sellButtons.get((int) (i + (currentPage * ITEMSPERPAGE)));//i);
                    Shape border = auctionhouseItemBorders.get((int) (i + (currentPage * ITEMSPERPAGE)));//i);

                    float itemnameX = (float) (border.getWidth() * 0.25 + border.getX());
                    float itemnameY = (float) (border.getHeight() * 0.25 + border.getY());
                    float priceX = itemnameX;
                    float priceY = (float) (border.getHeight() * 0.5 + border.getY());
                    float btnX = btn.getX() + borderMargin;
                    float btnY = btn.getY() + borderMargin;

//                    draw itemname, price, btn text
                    g.drawString(item.getItemId() + "", itemnameX, itemnameY);
                    g.drawString(item.getResourceAmount() + " - " + item.getResourceType(), priceX, priceY);
                    g.drawString("cancel", btnX, btnY);
                } else {
                    isCancelDone = true;
                }
                if (isCancelDone) {
                    double id = ((i + (currentPage * ITEMSPERPAGE)) - cancelItems.size());
                    if (id < sellItems.size()) {
                        Item item = sellItems.get((int) id);
                        Shape btn = sellButtons.get((int) (i + (currentPage * ITEMSPERPAGE)));

                        double borderId = (i + (currentPage * ITEMSPERPAGE));
                        Shape border = auctionhouseItemBorders.get((int) borderId);

                        float itemnameX = (float) (border.getWidth() * 0.25 + border.getX());
                        float itemnameY = (float) (border.getHeight() * 0.25 + border.getY());
                        float priceX = itemnameX;
                        float priceY = (float) (border.getHeight() * 0.5 + border.getY());
                        float btnX = btn.getX() + borderMargin;
                        float btnY = btn.getY() + borderMargin;

                        //draw itemname, price, btn text
                        g.drawString(item.getItemId() + "", itemnameX, itemnameY);
                        g.drawString(item.getResourceAmount() + " - " + item.getResourceType(), priceX, priceY);
                        g.drawString("sell", btnX, btnY);
                    }
                }
            }
        }
    }

    /**
     * Draw the buy view
     *
     * @param g
     */
    private void drawBuyView(Graphics g) {
        if (buyButtons != null) {
            for (int i = currentPage; i <= currentPage + ITEMSPERPAGE; i++) {
                if (i < buyButtons.size()) {
                    if ((i + (currentPage * ITEMSPERPAGE)) < buyButtons.size()) {
                        Shape s = buyButtons.get((int) (i + (currentPage * ITEMSPERPAGE)));//i);
                        g.draw(s);
                    }
                }
            }
        }

        int buyItemId = 0;
        if (buyItems != null
                && buyButtons != null
                && auctionhouseItemBorders != null) {

            for (int i = currentPage; i <= currentPage + ITEMSPERPAGE; i++) {
                if ((i + (currentPage * ITEMSPERPAGE)) < buyItems.size()) {
                    Item item = buyItems.get((int) (i + (currentPage * ITEMSPERPAGE)));
                    Shape btn = buyButtons.get((int) (i + (currentPage * ITEMSPERPAGE)));//i);
                    Shape border = auctionhouseItemBorders.get((int) (i + (currentPage * ITEMSPERPAGE)));//i);

                    float itemnameX = (float) (border.getWidth() * 0.25 + border.getX());
                    float itemnameY = (float) (border.getHeight() * 0.25 + border.getY());
                    float priceX = itemnameX;
                    float priceY = (float) (border.getHeight() * 0.5 + border.getY());
                    float btnX = btn.getX() + borderMargin;
                    float btnY = btn.getY() + borderMargin;

                    //draw itemname, price, btn text
                    g.drawString(item.getItemId() + "", itemnameX, itemnameY);
                    g.drawString(item.getResourceAmount() + " - " + item.getResourceType(), priceX, priceY);
                    g.drawString("buy", btnX, btnY);
                    buyItemId++;
                }
            }
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (game.getCurrentState() == this) {
            if (!inventorycalled) {
                handler = new BroadcastHandler(this);
                handler.sendInventoryRequest(new InventoryRequest(InventoryRequest.class.getSimpleName(), spaceshipid));
                handler.sendInventoryRequest(new InventoryRequest(InventoryRequest.class.getSimpleName(), -1));
                inventorycalled = true;
            }
        }
        this.inventory = SpaceClient.playerInventory;
        this.auctionhouseInventory = SpaceClient.auctionhouseInventory;

        updateAuctionView();
        Input input = container.getInput();
        checkMouseInput(input, game);
    }

    /**
     * Update the complete auctionhouse view
     */
    private void updateAuctionView() {
        int itemCount = 0;
        if (this.auctionhouseInventory != null) {
            if (buyView) {
                sellItems = new ArrayList<>();
                buyItems = new ArrayList<>();
                cancelItems = new ArrayList<>();
                for (Item item : auctionhouseInventory.getItems()) {
                    if (item.getRequestType() == AuctionHouseRequestType.BUY) {
                        itemCount++;
                        buyItems.add(item);
                    }
                }
                updateBuyView();
            } else if (sellView) {
                sellItems = new ArrayList<>();
                buyItems = new ArrayList<>();
                cancelItems = new ArrayList<>();
                for (Item item : auctionhouseInventory.getItems()) {
                    if (item.getRequestType() == AuctionHouseRequestType.SELL) {
                        itemCount++;
                        sellItems.add(item);
                    }
                    if (item.getRequestType() == AuctionHouseRequestType.CANCEL) {
                        cancelItems.add(item);
                        itemCount++;
                    }
                }
                updateSellView();
            }
        }
        updatePages();
    }

    /**
     * Create the buy buttons
     */
    private void updateBuyView() {
        auctionhouseItemBorders = new ArrayList<>();

        buyButtons = new ArrayList<>();
        double totalItems = 0;
        double itemPosition = 1;

        for (Item i : buyItems) {
            totalItems++;

            float borderY = (float) ((auctionhouseItemBase.getY() + auctionhouseItemBase.getHeight() - borderMargin) * (itemPosition)) + (2 * borderMargin);

            Shape autionItemBorder = new Rectangle(auctionhouseItemBase.getX(), borderY, auctionhouseItemBase.getWidth(), auctionhouseItemBase.getHeight());
            auctionhouseItemBorders.add(autionItemBorder);

            float buttonY = (int) (autionItemBorder.getY() + borderMargin);
            Shape buyButtonBorder = new Rectangle(auctionhouseButtonBase.getX(), buttonY, auctionhouseButtonBase.getWidth(), auctionhouseButtonBase.getHeight());
            buyButtons.add(buyButtonBorder);

            if (itemPosition == ITEMSPERPAGE + 1) {
                itemPosition = 0;
            }
            itemPosition++;
        }
        double tp = totalItems / (ITEMSPERPAGE + 1);
        totalPages = (int) Math.ceil(tp);
    }

    /**
     * Create the sell and cancel buttons
     */
    private void updateSellView() {
        auctionhouseItemBorders = new ArrayList<>();

        sellButtons = new ArrayList<>();
        double totalItems = 0;
        double itemPosition = 1;

        for (Item i : cancelItems) {
            float borderY = (float) ((auctionhouseItemBase.getY() + auctionhouseItemBase.getHeight() - borderMargin) * (itemPosition)) + (2 * borderMargin);
            Shape autionItemBorder = new Rectangle(auctionhouseItemBase.getX(), borderY, auctionhouseItemBase.getWidth(), auctionhouseItemBase.getHeight());
            auctionhouseItemBorders.add(autionItemBorder);

            float buttonY = (int) (autionItemBorder.getY() + borderMargin);
            Shape buyButtonBorder = new Rectangle(auctionhouseButtonBase.getX(), buttonY, auctionhouseButtonBase.getWidth(), auctionhouseButtonBase.getHeight());
            sellButtons.add(buyButtonBorder);

            if (itemPosition == ITEMSPERPAGE + 1) {
                itemPosition = 0;
            }
            itemPosition++;
            totalItems++;
        }
        for (Item i : sellItems) {
            float borderY = (float) ((auctionhouseItemBase.getY() + auctionhouseItemBase.getHeight() - borderMargin) * (itemPosition)) + (2 * borderMargin);
            Shape autionItemBorder = new Rectangle(auctionhouseItemBase.getX(), borderY, auctionhouseItemBase.getWidth(), auctionhouseItemBase.getHeight());
            auctionhouseItemBorders.add(autionItemBorder);

            float buttonY = (int) (autionItemBorder.getY() + borderMargin);
            Shape buyButtonBorder = new Rectangle(auctionhouseButtonBase.getX(), buttonY, auctionhouseButtonBase.getWidth(), auctionhouseButtonBase.getHeight());
            sellButtons.add(buyButtonBorder);

            if (itemPosition == ITEMSPERPAGE + 1) {
                itemPosition = 0;
            }
            itemPosition++;
            totalItems++;
        }

        double tp = totalItems / (ITEMSPERPAGE + 1);
        totalPages = (int) Math.ceil(tp);
    }

    /**
     * Create the page buttons
     */
    private void updatePages() {
        auctionhousePageBorders = new ArrayList<>();
        auctionhousePageBorders.add(auctionhousePageBase);
        for (int i = 1; i < totalPages; i++) {
            Shape lastPageBorder = auctionhousePageBorders.get(auctionhousePageBorders.size() - 1);
            float pageX = lastPageBorder.getX() + lastPageBorder.getWidth() + borderMargin;
            Shape page = new Rectangle(pageX, lastPageBorder.getY(), lastPageBorder.getWidth(), lastPageBorder.getHeight());
            auctionhousePageBorders.add(page);
        }
    }

    /**
     * Handles the input of the mouse
     *
     * @param input
     * @param game
     */
    private void checkMouseInput(Input input, StateBasedGame game) {
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            int mouseX = input.getMouseX();
            int mouseY = input.getMouseY();
            Vector2f mousePosition = new Vector2f(mouseX, mouseY);
            if (inBounds(mousePosition, exitPlanet)) {
                game.enterState(SpaceClient.STATE_GAME);
                buyView = false;
                this.sellView = false;
                currentPage = 0;
            } else if (inBounds(mousePosition, buyButton)) {
                this.buyView = true;
                this.sellView = false;
                currentPage = 0;
            } else if (inBounds(mousePosition, sellButton)) {
                this.buyView = false;
                this.sellView = true;
                currentPage = 0;
            } else {
            }

            updateBuyButtons(mousePosition);
            updateSellButtons(mousePosition);
            updatePageButtons(mousePosition);
        }
    }

    /**
     * Checks if the buy buttons are being pressed
     *
     * @param mousePosition the position of the mouse
     */
    private void updateBuyButtons(Vector2f mousePosition) {
        int buybtnId = 0;
        //if there are buy buttons
        if (buyButtons != null) {
            for (int i = currentPage; i <= currentPage + ITEMSPERPAGE; i++) {
                if (i < buyItems.size()) {
                    double id = (i + (currentPage * ITEMSPERPAGE));
                    if (id < buyItems.size()) {
                        Shape s = buyButtons.get((int) (i + (currentPage * ITEMSPERPAGE)));
                        //and the mouse pointer is in bounds of the butten
                        if (inBounds(mousePosition, s)) {
                            //cancel btn pressed
                            Item buyItem = buyItems.get((int) id);
                            if (handler != null) {
                                //cancel item to sell
                                handler.sendItem(buyItem);
                                System.out.println("BUY: " + buyItem.getItemId());
                            }
                        }
                    }
                }
                buybtnId++;
            }
        }
    }

    /**
     * Checks if the sell or cancel buttons are being pressed
     *
     * @param mousePosition the position of the mouse
     */
    private void updateSellButtons(Vector2f mousePosition) {
        //if there are sell buttons
        if (sellButtons != null) {
            boolean isCancelDone = false;
            for (int i = currentPage; i <= currentPage + ITEMSPERPAGE; i++) {
                //check if a cancel button has been pressed
                if (i < cancelItems.size()) {
                    double id = (i + (currentPage * ITEMSPERPAGE));
                    if (id < cancelItems.size()) {
                        Shape s = sellButtons.get((int) (i + (currentPage * ITEMSPERPAGE)));
                        //and the mouse pointer is in bounds of the butten
                        if (inBounds(mousePosition, s)) {
                            //cancel btn pressed
                            Item cancelItem = cancelItems.get((int) id);
                            if (handler != null) {
                                //cancel item to sell
                                handler.sendItem(cancelItem);
                                System.out.println("CANCEL: " + cancelItem.getItemId());
                            }
                        }

                    } else {
                        isCancelDone = true;
                    }
                    if (isCancelDone) {
                        double id2 = ((i + (currentPage * ITEMSPERPAGE)) - cancelItems.size());
                        if (id2 < sellItems.size()) {
                            Shape s = sellButtons.get((int) (i + (currentPage * ITEMSPERPAGE)));
                            //and the mouse pointer is in bounds of the butten
                            if (inBounds(mousePosition, s)) {
                                //cancel btn pressed
                                Item sellItem = sellItems.get((int) id2);
                                if (handler != null) {
                                    //cancel item to sell
                                    handler.sendItem(sellItem);
                                    System.out.println("SELL: " + sellItem.getItemId());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if a page button has been pressed
     *
     * @param mousePosition
     */
    private void updatePageButtons(Vector2f mousePosition) {
        int pagebtnId = 0;
        //if there are sell buttons
        if (auctionhousePageBorders != null) {
            for (Shape s : auctionhousePageBorders) {
                //and the mouse pointer is in bounds of the button
                if (inBounds(mousePosition, s)) {
                    //and the id is smaller then the size of the buttons
                    currentPage = pagebtnId;
                }
                pagebtnId++;
            }
        }
    }

    /**
     * Method to check if the mouse pointer is in bounds of a shape
     *
     * @param mousePosition the mouse position
     * @param shape the shape to check
     * @return
     */
    private boolean inBounds(Vector2f mousePosition, Shape shape) {
        if (mousePosition.x > shape.getX()
                && mousePosition.x < shape.getX() + shape.getWidth()
                && mousePosition.y > shape.getY()
                && mousePosition.y < shape.getY() + shape.getHeight()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void drawAfterDataReadFromSocketFromServer(List<GameObjectImpl> objects) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

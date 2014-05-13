/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.gui;

import com.ptsesd.groepb.shared.socket.InventoryRequest;
import com.yolosec.spaceclient.communication.BroadcastHandler;
import com.yolosec.spaceclient.dao.interfaces.DrawCallback;
import com.yolosec.spaceclient.game.player.Inventory;
import com.yolosec.spaceclient.game.player.Item;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
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
    private boolean inventorycalled = false;

    //private static final Rec
    //---------------------------------------------------------------------------------------------------------------
    PlanetState(int spaceshipId) {
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

//        //draw the inventory on 2/3 of the y-axis and on 1/3 of the x-axis
        int inventoryRectWidth = oneThirdOfScreenWidth - borderMargin;
        int inventoryRectHeight = (oneThirdOfScreenHeight * 2) - borderMargin;
        SLOTWIDTH = (inventoryRectWidth - (5 * borderMargin)) / 4;
        SLOTHEIGHT = (inventoryRectHeight - (5 * borderMargin)) / 4;

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
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        //g.drawRect(inventoryRectX, inventoryRectY, inventoryRectWidth, inventoryRectHeight);

        g.setColor(Color.cyan);
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
        g.setColor(Color.white);
        g.draw(auctionhouseBorder);
        if (buyView) {

        } else if (sellView) {

        }
        //g.drawString("Return to game", navigationRectX + borderMargin, navigationRectY + borderMargin);

        //g.drawRect(mainRectX, mainRectY, mainRectWidth, mainRectHeight);
    }

    private void drawInventorySlot(Graphics g, int slotid, Color color, Item item) {
        g.setColor(color);
        g.draw(new Ellipse(inventorySlots[slotid].getCenterX(), inventorySlots[slotid].getCenterY(), 10, 10));
        g.drawString(item.getResourceAmount() + "", inventorySlots[slotid].getX(), inventorySlots[slotid].getY());
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (game.getCurrentState() == this) {
            if (!inventorycalled) {
                BroadcastHandler handler = new BroadcastHandler(this);
                handler.sendInventoryRequest(new InventoryRequest(InventoryRequest.class.getSimpleName(), spaceshipid));
                inventorycalled = true;
            }
        }
        this.inventory = SpaceClient.playerInventory;

        Input input = container.getInput();
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            int mouseX = input.getMouseX();
            int mouseY = input.getMouseY();
            Vector2f mousePosition = new Vector2f(mouseX, mouseY);
            if (inBounds(mousePosition, exitPlanet)) {
                game.enterState(SpaceClient.STATE_GAME);
                buyView = false;
                this.sellView = false;
            } else if (inBounds(mousePosition, buyButton)) {
                this.buyView = true;
                this.sellView = false;
            } else if (inBounds(mousePosition, sellButton)) {
                this.buyView = false;
                this.sellView = true;
            }
//            if(inBounds(mouseX, mouseY, navigationRectX, navigationRectY, (navigationRectX + navigationRectWidth), (navigationRectY + navigationRectHeight))){
//                game.enterState(SpaceClient.STATE_GAME);
//            }
        }
    }

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

//    private boolean inBounds(int mouseX, int mouseY, int rectX, int rectY, int rectX2,
//            int rectY2) {
//        //check if in boundries
//        return mouseX > rectX && mouseX < rectX2 && mouseY > rectY
//                && mouseY < rectY2;
//    }
    @Override
    public void drawAfterDataReadFromSocketFromServer(List<GameObjectImpl> objects) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

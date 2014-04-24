/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yolosec.spaceclient.gui;

import com.yolosec.spaceclient.gui.GameState;
import com.yolosec.spaceclient.gui.SpaceClient;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Toon
 */
public class Planet extends BasicGameState{
    
    //---------------------------------------GUI LAYOUT ELEMENTS----------------------------------------------------
    private static final int borderMargin = 20;
    
    private static final int inventoryRectX = borderMargin, inventoryRectY = borderMargin;
    private static int inventoryRectWidth, inventoryRectHeight;
    
    private static final int navigationRectX = borderMargin;
    private static int navigationRectWidth, navigationRectHeight, navigationRectY;
    
    private static final int mainRectY = borderMargin;
    private static int mainRectX, mainRectWidth, mainRectHeight;
    //---------------------------------------------------------------------------------------------------------------

    @Override
    public int getID() {
        return 2;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        
        int oneThirdOfScreenWidth = (container.getScreenWidth() / 3);
        int oneThirdOfScreenHeight = (container.getScreenHeight()/ 3);
        
        //draw the inventory on 2/3 of the y-axis and on 1/3 of the x-axis
        inventoryRectWidth =  oneThirdOfScreenWidth - borderMargin;
        inventoryRectHeight = (oneThirdOfScreenHeight*2) - borderMargin;
        
        //draw the navigation section on 1/3 of the y-axis and on 1/3 of the x-axis
        navigationRectY = inventoryRectY + inventoryRectHeight + borderMargin;
                
        navigationRectWidth = oneThirdOfScreenWidth - (borderMargin);
        navigationRectHeight = oneThirdOfScreenHeight - (2*borderMargin);
        
        //x position is right of the first column
        mainRectX = inventoryRectX + inventoryRectWidth + borderMargin;
        //width is 2/3 of the screen plus margin
        mainRectWidth = (oneThirdOfScreenWidth*2) - (2*borderMargin);
        //height is complete screen plus margin
        mainRectHeight = container.getScreenHeight() - (2*borderMargin);
        
        //            MessagingGateway mg = new MessagingGateway("clientRequestorQueue", "clientReplierQueue") {
//
//                @Override
//                public void onReceivedMessage(ItemComm item) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//            };
//
//            mg.openConnection();
//            mg.sendItemComm(new ItemComm(1, 1, true));
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawRect(inventoryRectX, inventoryRectY, inventoryRectWidth, inventoryRectHeight);
        
        g.drawRect(navigationRectX, navigationRectY, navigationRectWidth, navigationRectHeight);
        g.drawString("Return to game", navigationRectX + borderMargin, navigationRectY + borderMargin);
        
        g.drawRect(mainRectX, mainRectY, mainRectWidth, mainRectHeight);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();
        
        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            int mouseX = input.getMouseX();
            int mouseY = input.getMouseY();
            
            if(inBounds(mouseX, mouseY, navigationRectX, navigationRectY, (navigationRectX + navigationRectWidth), (navigationRectY + navigationRectHeight))){
                game.enterState(SpaceClient.STATE_GAME);
            }
        }
    }
    
    private boolean inBounds(int mouseX, int mouseY, int rectX, int rectY, int rectX2,
            int rectY2) {
        //check if in boundries
        return mouseX > rectX && mouseX < rectX2 && mouseY > rectY
                && mouseY < rectY2;
    } 
}

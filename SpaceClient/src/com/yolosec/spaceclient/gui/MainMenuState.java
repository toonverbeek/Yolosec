/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yolosec.spaceclient.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Tim
 */
public class MainMenuState extends BasicGameState {
    
    private static final int quitRectX = 200;
    private static final int quitRectWidth = 100;
    
    private static final int quitRectY = 200;
    private static final int quitRectHeight = 50;
    
    private static int quitStringX, quitStringY;

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        //calculate the x and y, to be in the center of the quit rectangle
        quitStringX = (quitRectWidth / 2) - (container.getDefaultFont().getWidth("Quit [Q]") /2) + quitRectX;
        quitStringY = (quitRectHeight / 2) - (container.getDefaultFont().getLineHeight() /2) + quitRectY;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawString("menu state", 50, 50);
        
        g.drawRect(quitRectX, quitRectY, quitRectWidth, quitRectHeight);
        g.drawString("Quit [Q]", quitStringX, quitStringY);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();
        
        //if left mouse is clicked
        if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
            int mouseX = input.getMouseX();
            int mouseY = input.getMouseY();
            
            //if quit button is clicked
            if(inBounds(mouseX, mouseY, quitRectX, quitRectY, (quitRectX + quitRectWidth), (quitRectY + quitRectHeight))){
                container.exit();
            }
        }
        
        if (input.isKeyPressed(Input.KEY_Q)) {
            container.exit();
        }
    }
    
    private boolean inBounds(int mouseX, int mouseY, int rectX, int rectY, int rectX2,
            int rectY2) {
        //check if in boundries
        return mouseX > rectX && mouseX < rectX2 && mouseY > rectY
                && mouseY < rectY2;
    } 
}

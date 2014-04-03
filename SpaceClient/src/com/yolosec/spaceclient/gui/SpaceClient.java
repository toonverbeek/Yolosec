package com.yolosec.spaceclient.gui;

import com.yolosec.spaceclient.game.player.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import com.yolosec.spaceclient.game.world.GameWorldImpl;
import com.yolosec.spaceclient.game.player.Spaceship;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import javax.swing.JFrame;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.state.StateBasedGame;

public class SpaceClient extends StateBasedGame {

    private static final int FPS = 60;
    public static int screenHeight;
    public static int screenWidth;
    private boolean mainMenuIsOpen = false;

    public SpaceClient(String gamename) {
        super(gamename);
    }
    
    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new SpaceClient("Yolosec"));
            screenHeight = appgc.getScreenHeight();
            screenWidth = appgc.getScreenWidth();
            appgc.setDisplayMode(screenWidth, screenHeight, true);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(SpaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            if(mainMenuIsOpen == false) {
            JFrame frame = new JFrame();
            frame.setVisible(true);
            mainMenuIsOpen = true;
            this.enterState(1);
            } else {
                mainMenuIsOpen = false;
                this.enterState(0);
            }
        }
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        this.addState(new GameState());
        this.addState(new MainMenuState());
    }
}

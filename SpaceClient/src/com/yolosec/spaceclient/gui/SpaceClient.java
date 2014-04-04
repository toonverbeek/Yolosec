package com.yolosec.spaceclient.gui;

import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.jms.MessagingGateway;
import com.yolosec.spaceclient.game.player.User;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import javax.swing.JFrame;
import org.newdawn.slick.state.StateBasedGame;

public class SpaceClient extends StateBasedGame {

    private static final int FPS = 60;
    public static int screenHeight;
    public static int screenWidth;
    private boolean mainMenuIsOpen = false;
    public User user;

    public SpaceClient(String gamename, User user) {
        super(gamename);
        this.user = user;
    }
    
//    public static void main(String[] args) {
//        try {
//            AppGameContainer appgc;
//            appgc = new AppGameContainer(new SpaceClient("Yolosec"));
//            screenHeight = appgc.getScreenHeight();
//            screenWidth = appgc.getScreenWidth();
//            appgc.setDisplayMode(screenWidth, screenHeight, true);
//            appgc.start();
//        } catch (SlickException ex) {
//            Logger.getLogger(SpaceClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

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
        } else if (key == Input.KEY_T) {
            MessagingGateway mg = new MessagingGateway("clientRequestorQueue", "clientReplierQueue") {
                
                @Override
                public void onReceivedMessage(ItemComm item) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
            
            mg.openConnection();
            mg.sendItemComm(new ItemComm(1, 1, true));
        }
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        this.addState(new GameState(user));
        this.addState(new MainMenuState());
    }
}

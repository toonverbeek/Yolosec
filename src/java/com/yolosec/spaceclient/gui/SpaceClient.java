package com.yolosec.spaceclient.gui;

import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.jms.MessagingGateway;
import com.yolosec.auctionhouse.gui.AuctionHouseFrame;
import com.yolosec.spaceclient.game.player.User;
import com.yolosec.spaceclient.jms.ClientRequestorGateway;
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
    private AuctionHouseFrame ahFrame = new AuctionHouseFrame();
    public User user;
    private boolean gatewayStarted;

    /**
     * Create a new isntance of SpaceClient. The gameclient handles GUI logic
     * for the game, as well as menu states.
     *
     * @param gamename the name of the game, i.e. "Yolosec" (needed for
     * superclass)
     * @param user The user which owns this game, i.e. the player.
     */
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
    /**
     * Listens for, and handles key input.
     *
     * @param key the id of the key that was fired
     * @param c the character representing the key that was fired
     */
    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            if (mainMenuIsOpen == false) {
                JFrame frame = new JFrame();
                frame.setVisible(true);
                mainMenuIsOpen = true;
                this.enterState(1);
            } else {
                mainMenuIsOpen = false;
                this.enterState(0);
            }
        } else if (key == Input.KEY_T && !gatewayStarted) {
            if (!gatewayStarted) {
                ahFrame.setVisible(true);
            } else {
                gatewayStarted = !gatewayStarted;
                ahFrame.setVisible(false);
            }
        }
    }

    /**
     * Initializes a list of game states.
     *
     * @param container the container in which the game is running
     * @throws SlickException
     */
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        this.addState(new GameState(user));
        this.addState(new MainMenuState());
    }
}

package com.yolosec.spaceclient.gui;

//import com.ptsesd.groepb.shared.jms.MessagingGateway;
import com.yolosec.spaceclient.game.player.Inventory;
import com.yolosec.spaceclient.game.player.User;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class SpaceClient extends StateBasedGame {

    public static final int STATE_GAME = 0;
    private static final int STATE_MAINMENU = 1;
    private static final int STATE_PLANET = 2;

    private static final int FPS = 60;
    public static int screenHeight;
    public static int screenWidth;
    public static Inventory playerInventory;
    public static Inventory auctionhouseInventory;
    public User user;

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

    /**
     * Listens for, and handles key input.
     *
     * @param key the id of the key that was fired
     * @param c the character representing the key that was fired
     */
    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            //if not in the main menu
            if (this.getCurrentStateID() != STATE_MAINMENU) {
                //got the main menu state
                this.enterState(STATE_MAINMENU);
                Chat.showChat = false;
            } else {
                //go to the game state
                this.enterState(STATE_GAME);
                Chat.showChat = false;
            }
        }
        if (key == Input.KEY_F2) {
            if (this.getCurrentStateID() == STATE_PLANET) {
                this.enterState(STATE_GAME);
                Chat.showChat = false;
            } else {
                this.enterState(STATE_PLANET);
                Chat.showChat = false;
            }
        }
    }

    public void toPlanetState() {
        if (this.getCurrentStateID() != STATE_PLANET) {
            this.enterState(STATE_PLANET);
        } else {
            this.enterState(STATE_GAME);
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
        this.addState(new PlanetState(user.getSpaceship().getId()));
    }
}

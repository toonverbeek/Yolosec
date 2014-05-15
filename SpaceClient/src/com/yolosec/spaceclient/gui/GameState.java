/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yolosec.spaceclient.gui;

import com.yolosec.spaceclient.dao.interfaces.DrawCallback;
import com.yolosec.spaceclient.game.player.Inventory;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.game.player.User;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
import com.yolosec.spaceclient.game.world.GameWorldImpl;
import java.util.HashMap;
import java.util.List;
import org.newdawn.slick.AngelCodeFont;
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
public class GameState extends BasicGameState{
    
    private static final int FPS = 60;
    public static int screenHeight;
    public static int screenWidth;
    private User user;
    private GameWorldImpl gameWorld;
    private AngelCodeFont font;
    private HashMap<String, AngelCodeFont> fontSet = new HashMap<String, AngelCodeFont>();
    public static Inventory playerInventory;
    private StateBasedGame game;

    public GameState(User user){
        this.user = user;
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        try {
            this.game = game;
            container.setTargetFrameRate(FPS);
            container.setFullscreen(true);
//            user = new User(new Spaceship(10, 10), "Space_Invader1337");
            gameWorld = new GameWorldImpl(this, user);
            //Communicator.login(Serializer.serializeLogin(new LoginComm(LoginComm.class.getSimpleName(), "username", "password")));
            font = new AngelCodeFont("font.fnt", "font_0.png");
            AngelCodeFont resourceFont = new AngelCodeFont("font_resource.fnt", "font_resource_0.png");
            fontSet.put("resource_font", resourceFont);
        } catch (SlickException ex) {
                ex.printStackTrace();
            //Logger.getLogger(SpaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void toPlanetState() {
        ((SpaceClient) game).toPlanetState();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setFont(font);
        gameWorld.setFontSet(fontSet);
        gameWorld.render(g, false);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        gameWorld.update(container);
    }
}
package com.yolosec.spaceclient.gui;

import com.ptsesd.groepb.shared.LoginComm;
import com.ptsesd.groepb.shared.Serializer;
import com.yolosec.spaceclient.communication.Communicator;
import com.yolosec.spaceclient.game.player.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import com.yolosec.spaceclient.game.world.GameWorldImpl;
import com.yolosec.spaceclient.game.player.Spaceship;

public class SpaceClient extends BasicGame {

    private static final int FPS = 60;
    public static int screenHeight;
    public static int screenWidth;
    private User user;
    private GameWorldImpl gameWorld;

    public SpaceClient(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        gc.setTargetFrameRate(FPS);
        gc.setFullscreen(true);
        user = new User(new Spaceship(10, 10), "Space_Invader1337");
        gameWorld = new GameWorldImpl(user.getSpaceship());

    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        gameWorld.update(gc);
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        gameWorld.render(g, false);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new SpaceClient("Simple Slick Game"));
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
            System.exit(0);
        }
    }
}

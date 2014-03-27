package com.yolosec.spaceclient.gui;

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
import java.util.HashMap;
import org.newdawn.slick.AngelCodeFont;

public class SpaceClient extends BasicGame {

    private static final int FPS = 60;
    public static int screenHeight;
    public static int screenWidth;
    private User user;
    private GameWorldImpl gameWorld;
    private AngelCodeFont font;
    private HashMap<String, AngelCodeFont> fontSet = new HashMap<String, AngelCodeFont>();

    public SpaceClient(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc)  {
        try {
            gc.setTargetFrameRate(FPS);
            gc.setFullscreen(true);
            user = new User(new Spaceship(10, 10), "Space_Invader1337");
            gameWorld = new GameWorldImpl(user.getSpaceship());
            //Communicator.login(Serializer.serializeLogin(new LoginComm(LoginComm.class.getSimpleName(), "username", "password")));
            font = new AngelCodeFont("font.fnt", "font_0.png");
            AngelCodeFont resourceFont = new AngelCodeFont("font_resource.fnt", "font_resource_0.png");
            fontSet.put("resource_font", resourceFont);
        } catch (SlickException ex) {
            //Logger.getLogger(SpaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        gameWorld.update(gc);
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.setFont(font);
        gameWorld.setFontSet(fontSet);
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

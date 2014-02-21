package spaceclient.gui;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import spaceclient.game.Spaceship;
 
public class SpaceClient extends BasicGame {

    private static final int FPS = 60;
    private User player;

    public SpaceClient(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        gc.setTargetFrameRate(FPS);
        gc.setFullscreen(true);
        
        player = new User(new Spaceship(50, 50, new Rectangle(0,0,50,50)), "Space_Invader1337");
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        player.update(gc);
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        player.render(g);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new SpaceClient("Simple Slick Game"));
            appgc.setDisplayMode(appgc.getScreenWidth(), appgc.getScreenHeight(), true);
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

package spaceclient.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.util.mapped.MappedObject.map;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;
import spaceclient.communication.BroadcastHandler;
import spaceclient.dao.GameObjectDAOImpl;
import spaceclient.game.Camera;
import spaceclient.game.Spaceship;

public class SpaceClient extends BasicGame {

    private static final int FPS = 60;
    private static int tileCountWidth;
    private static int tileCountHeight;
    public static int screenHeight;
    public static int screenWidth;
    private User player;
    private List<Spaceship> spaceships = Collections.synchronizedList(new ArrayList<Spaceship>());
    private BroadcastHandler broadcastHandler;
    private TiledMap tileMap;
    private GameObjectDAOImpl gameObjectDAO;
    private int mapWidth;
    private int mapHeight;
    private int tileHeight;
    private int tileWidth;
    private Camera camera;
    private int mapX, mapY;

    public SpaceClient(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        gc.setTargetFrameRate(FPS);
        gc.setFullscreen(true);
        tileMap = new TiledMap("/newmap.tmx");
        mapWidth = tileMap.getWidth() * tileMap.getTileWidth();
        mapHeight = tileMap.getHeight() * tileMap.getTileHeight();
        tileHeight = tileMap.getTileHeight();
        tileWidth = tileMap.getTileWidth();
        gameObjectDAO = new GameObjectDAOImpl();
        player = new User(new Spaceship(60, 60, new Rectangle(60, 60, 50, 50)), "Space_Invader1337");
        camera = new Camera(tileMap, mapWidth, mapHeight);
        broadcastHandler = new BroadcastHandler(gameObjectDAO, player.getSpaceship());

        Thread t = new Thread(broadcastHandler);
        t.start();
        //broadcastHandler.login(this.player.getUsername(), "asdf");

    }

    private void initMap() {
        int displayList = GL11.glGenLists(1); //Save this int so you can access it later
        GL11.glNewList(displayList, GL11.GL_COMPILE);
        //Draw the tiles using Slick
        GL11.glEndList();

    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        player.update(gc);
        broadcastHandler.sendData(player.getSpaceship());
        Vector2f p = player.getSpaceship().getPosition();
        int xTile = (int) (p.x * tileCountHeight) / screenWidth;
        System.out.println(xTile);
        if (xTile < 0) {
            mapX++;
            //p.x = tileWidth;
        }
        if (xTile > tileWidth) {
            mapX--;

            //p.x = 0;
        }
        if (p.y < 0) {
            mapY--;
            //p.y = 0;
        }

        if (p.y > 0) {
            mapY++;
            //p.y = tileHeight;
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        int tileCountWidth = screenWidth / tileWidth;
        int tileCountHeight = screenHeight / tileHeight;
        tileMap.render((int) player.getSpaceship().getPosition().x + (tileWidth * 2), (int) player.getSpaceship().getPosition().y + (tileHeight * 2), mapX, mapY, mapX + tileCountWidth, mapY + tileCountHeight);
        //camera.translate(g, player.getSpaceship());
        player.render(g, true);
        for (Spaceship spaceship : gameObjectDAO.getSpaceships()) {
            
            spaceship.render(g, false);
        }
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.gui;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.ptsesd.groepb.shared.MessagingComm;
import com.ptsesd.groepb.shared.webservice.Message;
import com.yolosec.spaceclient.game.player.User;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Client;
import static javax.ws.rs.client.ClientBuilder.newClient;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Lisanne
 */
public class Chat extends GameObjectImpl implements Runnable {

    private int width;
    private int height;
    private Shape chatBox;
    private Vector2f chatBoxPosition;
    public static boolean showChat = false;
    private int enterCount = 1;
    private Client chatClient;
    private WebTarget allMessagesTarget;
    private WebTarget sendMessageTarget;
    private WebTarget loginTarget;
    private static String webserviceHostIP = "localhost:8080";
    private int maxCharactersPerMessage = 40;
    private int maxMessagesInBox = 10;
    private String message = "test";
    private Shape messageInputBox;
    private Shape allMessagesBox;
    private int bordermargin = 20;
    private ArrayList<MessagingComm> messages;
    private int updateTime = 0;

    private Font endfont = new Font("Verdana", Font.BOLD, 12);
    private TrueTypeFont finalEndFont = new TrueTypeFont(endfont, true);
    private org.newdawn.slick.Font startFont;
    private User user;
    private Gson gson = new Gson();
    private Thread sendMessageThread;
    private boolean updateMessages = false;
    private boolean writeMessage;

    public Chat(int x, int y, int width, int height, User user) {
        this.user = user;
        this.width = width;
        this.height = height;
        chatBoxPosition = new Vector2f(x, y);
        chatBox = new Rectangle(x, y, width, height);

        int allMessagesBoxX = (int) (chatBox.getX());
        int allMessagesBoxY = (int) (chatBox.getY() + bordermargin);
        int allMessagesBoxWidth = (int) chatBox.getWidth();
        int allMessagesBoxHeight = (int) chatBox.getHeight() - bordermargin;
        allMessagesBox = new Rectangle(allMessagesBoxX, allMessagesBoxY, allMessagesBoxWidth, allMessagesBoxHeight);

        System.out.println("MESSAGES IN BOX: " + allMessagesBoxHeight);
        double maxItems = (allMessagesBoxHeight / bordermargin);
        maxMessagesInBox = (int) Math.floor(maxItems);

        int inputMessageBoxX = (int) (allMessagesBox.getX());
        int inputMessageBoxY = (int) (allMessagesBox.getY() + allMessagesBox.getHeight() - bordermargin);
        int inputMessageBoxWidth = (int) chatBox.getWidth();
        int inputMessageBoxHeight = (int) chatBox.getHeight();
        messageInputBox = new Rectangle(inputMessageBoxX, inputMessageBoxY, inputMessageBoxWidth, inputMessageBoxHeight);

        //setup for the restfull api
        chatClient = newClient();
        allMessagesTarget = chatClient.target("http://" + webserviceHostIP + "/RestfullChatService/rest/messages/getClientMessages/");
        sendMessageTarget = chatClient.target("http://" + webserviceHostIP + "/RestfullChatService/rest/messages/sendClientMessage");
        loginTarget = chatClient.target("http://" + webserviceHostIP + "/RestfullChatService/rest/messages/login/");

        sendMessageThread = new Thread(new SendMessage());
    }

    public boolean getVisibility() {
        return this.showChat;
    }

    @Override
    public java.awt.Rectangle getRectangle() {
        return new java.awt.Rectangle();
    }

    public void update(GameContainer gc) {
        Input input = gc.getInput();
        //check if the chatbox must be open or closed
        if (input.isKeyPressed(Input.KEY_F1)) {
            if (enterCount > 1) {
                showChat = false;
                enterCount = 0;
            } else {
                showChat = true;
                messages = new ArrayList<>();
            }
            enterCount++;
        }
        if (showChat) {
            if (!message.isEmpty()) {
                if (input.isKeyPressed(Input.KEY_ENTER)) {
                    if (!sendMessageThread.isAlive()) {
                        sendMessageThread = new Thread(new SendMessage());
                        sendMessageThread.start();
                        getMessages();
                    }
                }
            }
            if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                if (inBounds(new Vector2f(input.getMouseX(), input.getMouseY()), chatBox)) {
                    writeMessage = true;
                    message = "";
                } else {
                    writeMessage = false;
                }
            }
            if (writeMessage) {
                if (message.length() <= maxCharactersPerMessage) {
                    message += getInputKeys(input);
                }
                if (input.isKeyPressed(Input.KEY_BACK)) {
                    if (message.length() > 0) {
                        message = message.substring(0, message.length() - 1);
                    }
                }
            }
        }
    }

    private String getInputKeys(Input input) {
        if (input.isKeyPressed(Input.KEY_A)) {
            return "a";
        } else if (input.isKeyPressed(Input.KEY_B)) {
            return "b";
        } else if (input.isKeyPressed(Input.KEY_C)) {
            return "c";
        } else if (input.isKeyPressed(Input.KEY_D)) {
            return "d";
        } else if (input.isKeyPressed(Input.KEY_E)) {
            return "e";
        } else if (input.isKeyPressed(Input.KEY_F)) {
            return "f";
        } else if (input.isKeyPressed(Input.KEY_G)) {
            return "g";
        } else if (input.isKeyPressed(Input.KEY_H)) {
            return "h";
        } else if (input.isKeyPressed(Input.KEY_I)) {
            return "i";
        } else if (input.isKeyPressed(Input.KEY_J)) {
            return "j";
        } else if (input.isKeyPressed(Input.KEY_K)) {
            return "k";
        } else if (input.isKeyPressed(Input.KEY_L)) {
            return "l";
        } else if (input.isKeyPressed(Input.KEY_M)) {
            return "m";
        } else if (input.isKeyPressed(Input.KEY_N)) {
            return "n";
        } else if (input.isKeyPressed(Input.KEY_O)) {
            return "o";
        } else if (input.isKeyPressed(Input.KEY_P)) {
            return "p";
        } else if (input.isKeyPressed(Input.KEY_Q)) {
            return "q";
        } else if (input.isKeyPressed(Input.KEY_R)) {
            return "r";
        } else if (input.isKeyPressed(Input.KEY_S)) {
            return "s";
        } else if (input.isKeyPressed(Input.KEY_T)) {
            return "t";
        } else if (input.isKeyPressed(Input.KEY_U)) {
            return "u";
        } else if (input.isKeyPressed(Input.KEY_V)) {
            return "v";
        } else if (input.isKeyPressed(Input.KEY_W)) {
            return "w";
        } else if (input.isKeyPressed(Input.KEY_X)) {
            return "x";
        } else if (input.isKeyPressed(Input.KEY_Y)) {
            return "y";
        } else if (input.isKeyPressed(Input.KEY_Z)) {
            return "z";
        } else if (input.isKeyPressed(Input.KEY_SPACE)) {
            return " ";
        } else {
            return "";
        }
    }

    /**
     * Method to check if the mouse pointer is in bounds of a shape
     *
     * @param mousePosition the mouse position
     * @param shape the shape to check
     * @return
     */
    private boolean inBounds(Vector2f mousePosition, Shape shape) {
        if (mousePosition.x > shape.getX()
                && mousePosition.x < shape.getX() + shape.getWidth()
                && mousePosition.y > shape.getY()
                && mousePosition.y < shape.getY() + shape.getHeight()) {
            return true;
        } else {
            return false;
        }
    }

    public void render(Graphics g, boolean b) {
        startFont = g.getFont();

        g.setFont(finalEndFont);
        if (showChat) {
            g.setColor(Color.orange);
            g.fill(chatBox, new GradientFill(chatBoxPosition.x, chatBoxPosition.y, Color.black, chatBoxPosition.x + width, chatBoxPosition.y + height, Color.black, true));
            g.draw(chatBox);
            g.drawString("chat", chatBoxPosition.x, chatBoxPosition.y);
            g.draw(allMessagesBox);
            g.draw(messageInputBox);
            int messageId = 0;
            for (int i = 0; i < messages.size(); i++) {
                MessagingComm messageComm = messages.get(i);
                messageId++;
                if (messageComm.getUsername().equals(user.getUsername())) {
                    g.setColor(Color.cyan);
                }
                String m = messageComm.getUsername() + " - " + messageComm.getMessage();
                g.drawString(m, allMessagesBox.getX(), allMessagesBox.getY() + ((messageId) + (bordermargin * messageId) - bordermargin));
                g.setColor(Color.white);
            }
            g.drawString(message, messageInputBox.getX(), messageInputBox.getY());
        }
        g.setFont(startFont);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            getMessages();
        }
    }

    private void getMessages() {
        updateTime++;
        if (showChat) {
            if (!updateMessages) {
                try {
                    //if (updateTime >= 5000) {
                    updateTime = 0;
                    updateMessages = true;
                    String get = allMessagesTarget.request().accept(MediaType.TEXT_PLAIN).get(String.class);
                    ArrayList<MessagingComm> tempItems = gson.fromJson(get, ArrayList.class);
                    messages = new ArrayList<>();
                    for (Object item : tempItems) {
                        Map itemlist = (LinkedTreeMap) item;
                        String header = (String) itemlist.get("header");
                        int spaceshipID = ((Double) itemlist.get("spaceShipId")).intValue();
                        String omessage = (String) itemlist.get("message");
                        String ousername = (String) itemlist.get("username");

                        MessagingComm itemToAdd = new MessagingComm(header, spaceshipID, omessage, ousername);
                        messages.add(itemToAdd);
                    }
                    Collections.reverse(messages);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                updateMessages = false;
            }
        }
    }

    class SendMessage implements Runnable {

        @Override
        public void run() {
            //message, spaceshipid, username
            Message m = new Message(message, user.getSpaceship().getId(), user.getUsername());
            try {
                sendMessageTarget.request().put(Entity.entity(m, MediaType.APPLICATION_XML), Message.class);
                message = "";
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

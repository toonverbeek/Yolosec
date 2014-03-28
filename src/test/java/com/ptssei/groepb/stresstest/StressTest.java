/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptssei.groepb.stresstest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JFrame;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author user
 */
public class StressTest extends TestCase {
    
    private JFrame guiFrame;
    
    private ExecutorService executor;
    private final int amountOfClients = 5;
    
    private boolean running = true;
    
    public StressTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //Instantiate variables
        executor = Executors.newFixedThreadPool(amountOfClients);
        
        //Set up GUI
        guiFrame = new JFrame();
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Example GUI");
        guiFrame.setSize(800,600);
        JButton newButton = new JButton("Close");
        newButton.setSize(100, 50);
        newButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("---[STRESSTEST] Clicked");
                running = false;
                System.out.println("---[STRESSTEST] Running [" + running +"]");
            }
        });
        
        guiFrame.add(newButton);
        
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * Test how many of the TestClient are initialized
     * Also start the clients for the next test
     */
    @Test
    public void initializeTestClient(){
        for (int i = 0; i < amountOfClients; i++) {
            boolean initiate = false;
            try {
                TestClientRunnable r = new TestClientRunnable();
                initiate = r.initiate();
                executor.execute(r);
            } catch (SocketException ex) {
                System.out.println(String.format("---[STRESSTEST] SocketException %s", ex.getMessage()));
            }
            assertEquals(true, initiate);
        }
    }
    
    /**
     *
     */
    @Test
    public void testClient(){
        guiFrame.setVisible(true);
        System.out.println("---[STRESSTEST] Start testClient");
        
      
        
        System.out.println("---[STRESSTEST] Stop testClient");
    }
    
    /**
     * Shutdown all the clients
     */
    @Test
    public void shutDownTestClient(){
        System.out.println("---[STRESSTEST] Shutting down");
        executor.shutdown();
        
        while(!executor.isTerminated()){
        }
        guiFrame.dispose();
        System.out.println("---[STRESSTEST] Shutted Down");
    }

    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.gui;

import com.ptsesd.groepb.shared.LoginComm;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.SpaceshipComm;
import com.yolosec.spaceclient.communication.Communicator;
import com.yolosec.spaceclient.dao.GameObjectDAOImpl;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.game.player.User;
import com.yolosec.spaceclient.game.world.Viewport;
import static com.yolosec.spaceclient.gui.SpaceClient.screenHeight;
import static com.yolosec.spaceclient.gui.SpaceClient.screenWidth;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Lisanne
 */
public class LoginForm extends javax.swing.JFrame {

    SpaceshipComm spaceshipComm = null;

    /**
     * Creates a new LoginForm which enables the user to enter his username and
     * password.
     */
    public LoginForm() {
        try {
            initComponents();
            setLocationRelativeTo(null);
            Communicator.initiate();
            l_wrong.setVisible(false);

        } catch (SocketException ex) {
            ex.printStackTrace();
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jtf_username = new javax.swing.JTextField();
        jp_password = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        l_wrong = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Login");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Username:");

        jLabel2.setText("Password:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setText("InSpace Client");

        l_wrong.setForeground(new java.awt.Color(255, 0, 0));
        l_wrong.setText("Something went wrong, try again; perhaps you used the wrong username/password?");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel2))
                                        .addGap(30, 30, 30)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jtf_username)
                                            .addComponent(jp_password, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(120, 120, 120)
                                .addComponent(jLabel3)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 53, Short.MAX_VALUE)
                        .addComponent(l_wrong)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtf_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jp_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(l_wrong)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        l_wrong.setVisible(false);
        handleLogin();

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * Gets the username and password from the associated input fields and c
     * constructs a new loginComm object and sends it to the server as Json in a
     * new thread.
     */
    private void handleLogin() {
        String username = jtf_username.getText();
        char[] password = jp_password.getPassword();

        String password2 = "";
        for (char c : password) {
            password2 += c;
        }

        Runnable receiver = new LoginListener();
        Thread t = new Thread(receiver);
        t.start();
        LoginComm lc = new LoginComm(LoginComm.class.getSimpleName(), username, password2);
        String json = Serializer.serializeLogin(lc);
        Communicator.sendLogin(json);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField jp_password;
    private javax.swing.JTextField jtf_username;
    private javax.swing.JLabel l_wrong;
    // End of variables declaration//GEN-END:variables

    /**
     * Listens for a login response from the server. If the value returned by
     * the server (of type SpaceshipComm) is not null, a new Spaceship object is
     * constructed as well as a new user. The gamescreen is then launched
     * alongside the gameworld.
     */
    class LoginListener implements Runnable {

        @Override
        public void run() {
            try {
                spaceshipComm = Communicator.receiveLogin();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (spaceshipComm != null && spaceshipComm.getId() != -1) {
                    AppGameContainer appgc;
                    Spaceship s = new Spaceship(spaceshipComm);
                    Viewport.spaceship = s;
                    System.out.println("--set yolosec spaceship");
                    User user = new User(s, jtf_username.getText());
                    SpaceClient client = new SpaceClient("Yolosec", user);
                    appgc = new AppGameContainer(client);

                    for (int r : s.getResources()) {
                        System.out.println(r);
                    }
                    System.out.println("----------");
                    screenHeight = appgc.getScreenHeight();
                    screenWidth = appgc.getScreenWidth();
                    appgc.setDisplayMode(screenWidth, screenHeight, true);
                    appgc.start();
                    setVisible(false);
                } else {
                    l_wrong.setVisible(true);
                }
            } catch (SlickException ex) {
                ex.printStackTrace();
            }
        }
    }
}
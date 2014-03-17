/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import shared.GamePacket;

/**
 *
 * @author Toon
 */
public class LoginComm extends GamePacket {

    private String username, password;

    public LoginComm(String header, String username, String password) {
        super(header);
        this.username = username;
        this.password = password;
    }

}

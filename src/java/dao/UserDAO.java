/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import com.ptsesd.groepb.shared.User;

/**
 *
 * @author Administrator
 */
public interface UserDAO {
    
    public boolean registerUser(User user);
    
    public boolean edit(User user);
    
    public boolean login(String username, String password);

    public User find(long userId);
    
    User find(String username);
}

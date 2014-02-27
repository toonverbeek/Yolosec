/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.User;
import java.util.List;

/**
 *
 * @author Lisanne
 */
public interface UserDAO {

    int count();

    void create(User user);

    void edit(User user, User user2);

    List<User> findAll();

    User find(String username);

    User find(Long id);

    void remove(User user);
}

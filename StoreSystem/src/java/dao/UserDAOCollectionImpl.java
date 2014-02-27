/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lisanne
 */
public class UserDAOCollectionImpl implements UserDAO {

    private List<User> users = new ArrayList<>();

    public UserDAOCollectionImpl() {
    }

    /**
     * Get the amount of users
     * @return 
     */
    @Override
    public int count() {
        if (users != null) {
            return this.users.size();
        }
        return 0;
    }

    /**
     * Create a new user
     * @param user 
     */
    @Override
    public void create(User user) {
        if (user != null) {
            if (!users.contains(user)) {
                for (User u : users) {
                    if (u.getUsername().equals(user.getUsername())) {
                        break;
                    }
                }
                users.add(user);
            }
        }
    }

    /**
     * Edit a user
     * @param user the old user
     * @param user2 the new user
     */
    @Override
    public void edit(User user, User user2) {
        if (user != null && user2 != null) {
            if (users.contains(user)) {
                users.remove(user);
                users.add(user2);
            }
        }
    }

    /**
     * Get all the users
     * @return 
     */
    @Override
    public List<User> findAll() {
        return this.users;
    }

    /**
     * Get a user with a username
     * @param username the username of the wanted user
     * @return 
     */
    @Override
    public User find(String username) {
        if (!username.isEmpty()) {
            for (User u : users) {
                if (u.getUsername().equals(username)) {
                    return u;
                }
            }
        }
        return null;
    }

    /**
     * Remove a user
     * @param user the user to remove
     */
    @Override
    public void remove(User user) {
        if (user != null) {
            if (users.contains(user)) {
                users.remove(user);
            }
        }
    }

    @Override
    public User find(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

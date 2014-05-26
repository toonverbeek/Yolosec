/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.ptsesd.groepb.shared.User;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Administrator
 */
public class UserDAO_Impl implements UserDAO {

    EntityManager em;

    public UserDAO_Impl(EntityManager em) {

        this.em = em;
    }
    
    @Override
    public boolean edit(User user){
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.close();
            return false;
        }
    }

    @Override
    public boolean registerUser(User user) {
        em.getTransaction().begin();
        if (!em.contains(user)) {
            em.persist(user);
            em.flush();
        }
        em.getTransaction().commit();
        return true;
    }

    @Override
    public User find(long id) {
        User find = em.find(User.class, id);
        return find;
    }
    
    @Override
    public User find(String username) {
        TypedQuery<User> createQuery = em.createQuery("SELECT u FROM User u WHERE u.username = ?1", User.class);
        createQuery.setParameter(1, username);
        return createQuery.getSingleResult();
    }

    @Override
    public boolean login(String username, String password) {
        TypedQuery<User> createQuery = em.createQuery("SELECT u FROM User u WHERE u.username = ?1 AND u.password = ?2", User.class);
        createQuery.setParameter(1, username);
        createQuery.setParameter(2, password);
        try {
            User singleResult = createQuery.getSingleResult();
            if (singleResult != null) {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }
}

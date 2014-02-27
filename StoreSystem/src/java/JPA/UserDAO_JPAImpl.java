/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA;

import dao.UserDAO;
import domain.User;
import java.util.*;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.persistence.*;

/**
 *
 * @author Lisanne
 */
@Alternative
@Stateless
public class UserDAO_JPAImpl implements UserDAO {

    @PersistenceContext
    private EntityManager em;

    @PersistenceUnit
    EntityManagerFactory emf;

    List<User> users = new ArrayList<User>();

    public UserDAO_JPAImpl() {
        emf = Persistence.createEntityManagerFactory("StoreSystemPU");
        em = emf.createEntityManager();
    }

    @Override
    public int count() {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(User user) {
            em.persist(user);
    }

    @Override
    public void edit(User user, User user2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<User> findAll() {
        List<User> result = em.createQuery( "from user", User.class ).getResultList();
        return result;
    }

    @Override
    public User find(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User find(Long id) {
        try {
            User user = em.find(User.class, id);
            return user;
        } catch (Exception ex) {
            throw ex;
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA;

import dao.UserDAO;
import domain.Account;
import domain.Spaceship;
import domain.Test;
import java.util.*;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.faces.bean.SessionScoped;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

/**
 *
 * @author Lisanne
 */
@Stateless
public class UserDAO_JPAImpl implements UserDAO {

    @PersistenceContext
    private EntityManager em;

    @PersistenceUnit
    EntityManagerFactory emf;

    List<Account> users = new ArrayList<Account>();

    public UserDAO_JPAImpl() {
        emf = Persistence.createEntityManagerFactory("StoreSystemPU");
        em = emf.createEntityManager();
        em.setFlushMode(FlushModeType.COMMIT);
    }

    @Override
    public int count() {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional
    public void create(Account user) {
        System.out.println("persisting");
        em.persist(user);
        System.out.println(user.toString());
        System.out.println("persisting DONE");
    }

    @Override
    public void edit(Account user, Account user2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional
    public List<Account> findAll() {
//        TypedQuery<Account> q = em.createQuery("select a from accountentity a", Account.class);
//        List<Account> result = q.getResultList();

        CriteriaBuilder cBuilder = em.getCriteriaBuilder();
        CriteriaQuery cQuery = cBuilder.createQuery(Account.class);
        Root<Account> root = cQuery.from(Account.class);
        cQuery.select(root);
        TypedQuery<Account> tQuery = em.createQuery(cQuery);

        List<Account> accounts = tQuery.getResultList();
        System.out.println(accounts.size());

        return accounts;
    }

    @Override
    public Account find(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Account user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Account find(Long id) {
        try {
            Account user = em.find(Account.class, id);
            return user;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    @Transactional
    public void createTest(Test test) {
        System.out.println("persisting");
        em.merge(test);
        System.out.println("PERSISTED");
    }

}

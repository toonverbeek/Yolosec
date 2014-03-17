/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA;

import annotations.AccountJPAImpl;
import dao.UserDAO;
import domain.Account;
import java.util.*;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Lisanne
 */
@Stateless
@AccountJPAImpl
public class UserDAO_JPAImpl implements UserDAO {

    @PersistenceContext
    private EntityManager em;

    List<Account> users = new ArrayList<Account>();

    @Override
    public int count() {
        return findAll().size();
    }

    @Override
    public void create(Account user) {
        em.persist(user);
    }

    @Override
    public void edit(Account user) {
        em.merge(user);
    }

    @Override
    public List<Account> findAll() {

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
        for (Account a : findAll()) {
            if (a.getUsername().equals(username)) {
                return em.find(Account.class, a.getId());
            }
        }
        return null;
    }

    @Override
    public void remove(Account user) {
        Account toBeRemovedUser = em.merge(user);
        em.remove(toBeRemovedUser);
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
}

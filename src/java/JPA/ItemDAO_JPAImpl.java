/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA;

import annotations.AccountJPAImpl;
import annotations.ItemJPAImpl;
import dao.ItemDAO;
import domain.Account;
import domain.Item;
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
@ItemJPAImpl
public class ItemDAO_JPAImpl implements ItemDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public int count() {
        return findAll().size();
    }

    @Override
    public void create(Item item) {
        em.persist(item);
    }

    @Override
    public void edit(Item item) {
        em.merge(item);
    }

    @Override
    public List<Item> findAll() {
        
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();
        CriteriaQuery cQuery = cBuilder.createQuery(Item.class);
        Root<Account> root = cQuery.from(Item.class);
        cQuery.select(root);
        TypedQuery<Item> tQuery = em.createQuery(cQuery);

        List<Item> items = tQuery.getResultList();

        return items;
    }

    @Override
    public Item find(String itemname) {
         for (Item i : findAll()) {
            if (i.getName().equals(itemname)) {
                return em.find(Item.class, i.getId());
            }
        }
        return null;
    }

    @Override
    public Item find(Long id) {
        return em.find(Item.class, id);
    }

    @Override
    public void remove(Item item) {
        em.remove(item);
    }
}

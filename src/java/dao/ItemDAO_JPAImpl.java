/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.ptsesd.groepb.shared.Item;
import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.User;
import com.ptsesd.groepb.shared.UserItem;
import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Lisanne
 */
public class ItemDAO_JPAImpl implements ItemDAO {

    EntityManager em;

    public ItemDAO_JPAImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public int count() {
        return findAll().size();
    }

    @Override
    public void create(Item item) {
        try {
            em.getTransaction().begin();
            em.persist(item);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.close();
            throw ex;
        }
    }

    @Override
    public void edit(Item item) {
        try {
            em.getTransaction().begin();
            em.merge(item);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.close();
            throw ex;
        }
    }

    @Override
    public List<UserItem> findAllUserItem() {
        Query createQuery = em.createQuery("SELECT i FROM UserItem i");
        return createQuery.getResultList();
    }

    @Override
    public List<UserItem> findAllUserItem(User user) {
        TypedQuery<UserItem> createQuery = em.createQuery("SELECT i FROM UserItem i WHERE i.user = :user", UserItem.class);
        createQuery.setParameter("user", user);
        List<UserItem> resultList = createQuery.getResultList();
        return resultList;
    }

    @Override
    public List<Item> findAll() {
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();
        CriteriaQuery cQuery = cBuilder.createQuery(Item.class);
        Root<Item> root = cQuery.from(Item.class);
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
        try {
            em.getTransaction().begin();
            em.remove(item);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.close();
            throw ex;
        }
    }

    @Override
    public void closeEntityManager() {
        em.close();
    }

    @Override
    public List<ItemComm> getInventory(User user) {
        List<UserItem> userItems = findAllUserItem(user);
        List<ItemComm> items = new ArrayList<>();
        for (UserItem uItem : userItems) {
            Item item = find(uItem.getItem().getId());
            ItemComm ic = new ItemComm(item, user.getUserId(), null);
            items.add(ic);
        }
        return items;
    }

    @Override
    public void edit(UserItem item) {
        try {
            em.getTransaction().begin();
            em.merge(item);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.close();
            throw ex;
        }
    }

    @Override
    public UserItem find(Item item) {
        TypedQuery<UserItem> createQuery = em.createQuery("SELECT u FROM UserItem u WHERE u.item = ?1", UserItem.class);
        createQuery.setParameter(1, item);
        UserItem singleResult = createQuery.getSingleResult();
        return singleResult;
    }

    @Override
    public boolean buyItem(User user, Item item) {
        try {
            UserItem userItem = find(item);
            userItem.setUser(user);
            edit(userItem);
            return true;
        }catch(Exception ex){
            return false;
        }
    }
}

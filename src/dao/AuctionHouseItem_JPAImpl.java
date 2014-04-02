/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.AuctionHouseItem;
import domain.Item;
import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Lisanne
 */
public class AuctionHouseItem_JPAImpl implements AuctionHouseItemDAO {

    private static final String PERSISTENCE_UNIT_NAME = "EconomySystemPU";
    private static EntityManagerFactory factory;

    EntityManager em;

    public AuctionHouseItem_JPAImpl() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
    }

    @Override
    public int count() {
        return findAll().size();
    }

    @Override
    public boolean create(AuctionHouseItem item) {
        try {
            em.getTransaction().begin();
            em.persist(item);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.close();
            throw ex;
        }
    }

    @Override
    public void edit(AuctionHouseItem item) {
        //TODO Transactions
        em.merge(item);
    }

    @Override
    public List<AuctionHouseItem> findAll() {
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();
        CriteriaQuery cQuery = cBuilder.createQuery(AuctionHouseItem.class);
        Root<AuctionHouseItem> root = cQuery.from(AuctionHouseItem.class);
        cQuery.select(root);
        TypedQuery<AuctionHouseItem> tQuery = em.createQuery(cQuery);

        List<AuctionHouseItem> items = tQuery.getResultList();

        return items;
    }

    @Override
    public AuctionHouseItem find(String itemname) {
        for (AuctionHouseItem i : findAll()) {
            if (i.getItem().getName().equals(itemname)) {
                return em.find(AuctionHouseItem.class, i.getId());
            }
        }
        return null;
    }

    @Override
    public AuctionHouseItem find(Long id) {
        //TODO Transactions ?
        return em.find(AuctionHouseItem.class, id);
    }

    @Override
    public void remove(AuctionHouseItem item) {
        //TODO Transactions
        em.remove(item);
    }

    @Override
    public void closeEntityManager() {
        em.close();
    }
}

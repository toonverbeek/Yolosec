/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Item;
import java.util.List;

/**
 *
 * @author Lisanne
 */
public interface ItemDAO {

    int count();

    void create(Item item);

    void edit(Item item);

    List<Item> findAll();

    Item find(String itemname);

    Item find(Long id);

    void remove(Item item);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Account;
import java.util.List;

/**
 *
 * @author Lisanne
 */
public interface UserDAO {

    int count();

    void create(Account user);

    void edit(Account user);

    List<Account> findAll();

    Account find(String username);

    Account find(Long id);

    void remove(Account user);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import annotations.AccountJPAImpl;
import annotations.ItemJPAImpl;
import dao.ItemDAO;
import dao.UserDAO;
import domain.Account;
import domain.Item;
import domain.Resource;
import domain.Spaceship;
import domain.Stat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author Toon
 */
@WebService
public class Webservice {

    @AccountJPAImpl
    @Inject
    UserDAO userDAO;

    @ItemJPAImpl
    @Inject
    ItemDAO itemDAO;
    private Account loggedInAccount;

    public String hello() {
        return "hello";
    }

    public String hellohello() {
        return "hello";
    }

    @PostConstruct
    private void initItems() {
        List<Resource> item1Resources = new ArrayList<>();
        item1Resources.add(new Resource("Mineral", 100));
        item1Resources.add(new Resource("Iron", 50));
        List<Stat> item1Stats = new ArrayList<>();
        item1Stats.add(new Stat("Stamina", 10));

        String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis hendrerit velit sit amet ligula faucibus, a aliquam dui consequat. Sed egestas mauris gravida sem commodo tristique. Maecenas sed mauris metus. Vestibulum nunc nunc, pretium et interdum eget, tincidunt ut eros. Suspendisse in urna nec enim cursus fermentum. Quisque ultricies eros laoreet, molestie odio vitae, congue nisl. Praesent eget eleifend lectus.";

        Item item1 = new Item("Item1", lorem, item1Resources, item1Stats, "");
        Item item2 = new Item("Item 2", lorem, item1Resources, item1Stats, "");
        Item item3 = new Item("Item 3", lorem, item1Resources, item1Stats, "");
        Item item4 = new Item("Item 4", lorem, item1Resources, item1Stats, "");

        this.itemDAO.create(item1);
        this.itemDAO.create(item2);
        this.itemDAO.create(item3);
        this.itemDAO.create(item4);

        Account a = new Account("Jamy", "Jamy", new Spaceship(null), item1Resources);
        userDAO.create(a);
    }

    @WebMethod
    public Boolean buyItem(long itemId, long userId) {
        System.out.println("AccountID: " + userId + " - ItemID: " + itemId);
        Account account = userDAO.find(userId);
        Item itemToBuy = itemDAO.find(itemId);

        Boolean isSuccesful = true;

        for (Resource ritem : itemToBuy.getResources()) {
            for (Resource raccount : account.getResources()) {
                //if the type of the resources match ...
                if (ritem.getType().equals(raccount.getType())) {
                    if (raccount.getAmount() < ritem.getAmount()) {
                        //if ammount is not enough. make boolean false
                        isSuccesful = false;
                    }
                }
            }
        }

        if (isSuccesful) {
            for (Resource ritem : itemToBuy.getResources()) {
                for (Resource raccount : account.getResources()) {
                    //if the type of the resources match ...
                    if (ritem.getType().equals(raccount.getType())) {
                        if (raccount.getAmount() >= ritem.getAmount()) {
                            //reduce the resource of the player by the amount of resources of the item
                            raccount.setAmount(raccount.getAmount() - ritem.getAmount());
                        }
                    }
                }
            }

            //add the item to the inventory of the currently loggedin user
            account.addItemToSpaceShipInventory(itemToBuy);
            userDAO.edit(account);

            //Success
        } else {
            //Fail
        }

        return isSuccesful;

    }

    public boolean registerAccount(String username, String password) {
        //test purposes
        List<Resource> item1Resources = new ArrayList<>();
        item1Resources.add(new Resource("Common", 100));
        item1Resources.add(new Resource("Magic", 100));
        item1Resources.add(new Resource("Rare", 100));
        item1Resources.add(new Resource("SpaceCoins", 100));

        for (Account u : userDAO.findAll()) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }

        return this.userDAO.create(new Account(username, password, new Spaceship(new ArrayList<Item>()), item1Resources));
    }

    @WebMethod
    public String login(String username, String password) {
        List<Account> users = userDAO.findAll();
        for (Account u : users) {
            if (u.getUsername().equals(username)) {
                if (u.getPassword().equals(password)) {
                    return u.getUsername();
                }
            }
        }
        return null;
    }

    public boolean addResourceToUser(Resource resource, Account account) {
        account.editResource("SpaceCoins", resource.getAmount());
        return userDAO.edit(loggedInAccount);
    }
    
    public List<Item> getResourcesForUser(Account account){
        return account.getSpaceShipItems();
    }
}

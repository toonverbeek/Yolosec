package service;

import annotations.ItemJPAImpl;
import annotations.AccountJPAImpl;
import dao.ItemDAO;
import dao.UserDAO;
import domain.Item;
import domain.Resource;
import domain.Spaceship;
import domain.Stat;
import domain.Account;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class StoreService implements Serializable {

    @Inject
    @AccountJPAImpl
    private UserDAO userDAO;

    @Inject
    @ItemJPAImpl
    private ItemDAO itemDAO;

    private Account loggedInAccount;

    public StoreService() {
    }

    @PostConstruct
    private void initItems() {

        List<Resource> item1Resources = new ArrayList<>();
        item1Resources.add(new Resource("Mineral", 100));
        item1Resources.add(new Resource("Iron", 50));
        List<Stat> item1Stats = new ArrayList<>();
        item1Stats.add(new Stat("Stamina", 10));

        String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis hendrerit velit sit amet ligula faucibus, a aliquam dui consequat. Sed egestas mauris gravida sem commodo tristique. Maecenas sed mauris metus. Vestibulum nunc nunc, pretium et interdum eget, tincidunt ut eros. Suspendisse in urna nec enim cursus fermentum. Quisque ultricies eros laoreet, molestie odio vitae, congue nisl. Praesent eget eleifend lectus.";

        Item item1 = new Item("Item 1", lorem, item1Resources, item1Stats, false, "");
        Item item2 = new Item("Item 2", lorem, item1Resources, item1Stats, false, "");
        Item item3 = new Item("Item 3", lorem, item1Resources, item1Stats, false, "");
        Item item4 = new Item("Item 4", lorem, item1Resources, item1Stats, false, "");

        this.itemDAO.create(item1);
        this.itemDAO.create(item2);
        this.itemDAO.create(item3);
        this.itemDAO.create(item4);
    }

    /**
     * Get the mockup items
     *
     * @return
     */
    public List<Item> getMockupItems() {
        return itemDAO.findAll();
    }

    /**
     * Get all the users
     *
     * @return
     */
    public Collection<Account> getUsers() {
        return this.userDAO.findAll();
    }

    /**
     * Create a user
     *
     * @param user the user to create
     * @return
     */
    public boolean createUser(Account user) {
        for (Account u : getUsers()) {
            if (u.getUsername().equals(user.getUsername())) {
                return false;
            }
        }
        userDAO.create(user);
        return true;
    }

    /**
     * Get the amount of users
     *
     * @return
     */
    public long getUserCount() {
        return userDAO.count();
    }

    /**
     * Get a user with username
     *
     * @param username the name to search
     * @return
     */
    public Account getUser(String username) {
        return userDAO.find(username);
    }

    /**
     * Get all the items of a user
     *
     * @param user a user which contains a spaceship
     * @return
     */
    public Collection<Item> getItemsFromUser(Account user) {
        return this.userDAO.find(user.getUsername()).getSpaceShipItems();
    }

    /**
     * Add item to inventory of user
     *
     * @param user a user which contains a spaceship
     * @param item the item to add
     */
    public void addItemToSpaceship(Account user, Item item) {
        user.addItemToSpaceShipInventory(item);
        userDAO.edit(user);
    }

    /**
     * Get the amount of items of the user
     *
     * @param user a user which contains a spaceship
     * @return
     */
    public long getItemCount(Account user) {
        return this.userDAO.find(user.getUsername()).getSpaceShipItems().size();
    }

    /**
     * Get items with name from the inventory of the user
     *
     * @param user a user which contains a spaceship
     * @param name the name to search
     * @return
     */
    public Collection<Item> getItemsWithName(Account user, String name) {
        return this.userDAO.find(user.getUsername()).getSpaceShipItems();
    }

    /**
     * Get items with name from the inventory of the user
     *
     * @param user a user which contains a spaceship
     * @param id the id to search
     * @return
     */
    public Item getItemWithId(Account user, long id) {
        return user.getSpaceship().findItem(id);
    }

    public boolean registerUser(String username, String password1, String password2) {
        List<Resource> item1Resources = new ArrayList<>();
        item1Resources.add(new Resource("Mineral", 200));
        item1Resources.add(new Resource("Iron", 100));

        Account user = new Account(username, password1, new Spaceship(new ArrayList<Item>()), item1Resources);
        Item userItem = new Item("TEST", "TEST", item1Resources, null, false, "");
        this.itemDAO.create(userItem);
        user.addItemToSpaceShipInventory(userItem);
        return createUser(user);
    }

    public boolean login(String username, String password) {
        List<Account> users = userDAO.findAll();
        for (Account u : users) {
            if (u.getUsername().equals(username)) {
                this.loggedInAccount = u;
                return u.getPassword().equals(password);
            }
        }
        return false;
    }

    public Account getLoggedInAccount() {
        return loggedInAccount;
    }

    public List<Item> getMyItems() {
        return this.userDAO.find(loggedInAccount.getUsername()).getSpaceShipItems();
    }

    public List<Item> getAllItems() {
        return itemDAO.findAll();
    }

    public void buyItem(Item selectedItem) {
        loggedInAccount = userDAO.find(loggedInAccount.getUsername());
        for (Resource ritem : selectedItem.getResources()) {
            for (Resource raccount : loggedInAccount.getResources()) {
                //if the type of the resources match ...
                if (ritem.getType().equals(raccount.getType())) {
                    if (raccount.getAmount() >= ritem.getAmount()) {
                        //add the item to the inventory of the currently loggedin user
                        loggedInAccount.addItemToSpaceShipInventory(selectedItem);
                        //reduce the resource of the player by the amount of resources of the item
                        raccount.setAmount(raccount.getAmount() - ritem.getAmount());
                    }
                }
            }
        }
        userDAO.edit(loggedInAccount);
    }
}

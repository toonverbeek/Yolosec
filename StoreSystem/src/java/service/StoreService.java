package service;

import JPA.UserDAO_JPAImpl;
import dao.UserDAO;
import dao.UserDAOCollectionImpl;
import domain.Item;
import domain.Resource;
import domain.Spaceship;
import domain.Stat;
import domain.Account;
import domain.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class StoreService {

    //private UserDAO userDAO = new UserDAOCollectionImpl();
    private UserDAO userDAO = new UserDAO_JPAImpl();

    private List<Item> mockupItems = new ArrayList<>();

    public StoreService() {
//        initUsers();
        initItems();
        
        userDAO.createTest(new Test());
    }

    private void initUsers() {
        Account u1 = new Account("Demo", "Demo", new Spaceship(), new ArrayList<Resource>());
        Account u2 = new Account("Demo2", "Demo", new Spaceship(), new ArrayList<Resource>());
        Account u3 = new Account("asdfasdf", "asdfasdf", new Spaceship(), new ArrayList<Resource>());

        userDAO.create(u1);
        userDAO.create(u2);
        userDAO.create(u3);
    }

    private void initItems() {
        List<Resource> item1Resources = new ArrayList<>();
        item1Resources.add(new Resource("Mineral", 100));
        item1Resources.add(new Resource("Iron", 50));
        List<Stat> item1Stats = new ArrayList<>();
        item1Stats.add(new Stat("Stamina", 10));

        String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis hendrerit velit sit amet ligula faucibus, a aliquam dui consequat. Sed egestas mauris gravida sem commodo tristique. Maecenas sed mauris metus. Vestibulum nunc nunc, pretium et interdum eget, tincidunt ut eros. Suspendisse in urna nec enim cursus fermentum. Quisque ultricies eros laoreet, molestie odio vitae, congue nisl. Praesent eget eleifend lectus.";

        Item item1 = new Item(0, "Item 1", lorem, item1Resources, item1Stats, false, "");
        Item item2 = new Item(1, "Item 2", lorem, item1Resources, item1Stats, false, "");
        Item item3 = new Item(2, "Item 3", lorem, item1Resources, item1Stats, false, "");
        Item item4 = new Item(3, "Item 4", lorem, item1Resources, item1Stats, false, "");

        this.mockupItems.add(item1);
        this.mockupItems.add(item2);
        this.mockupItems.add(item3);
        this.mockupItems.add(item4);

    }

    /**
     * Get the mockup items
     *
     * @return
     */
    public List<Item> getMockupItems() {
        return this.mockupItems;
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
        return user.getSpaceship().getAllItems();
    }

    /**
     * Add item to inventory of user
     *
     * @param user a user which contains a spaceship
     * @param item the item to add
     */
    public void addItemToSpaceship(Account user, Item item) {
        user.getSpaceship().addItemToInventory(item);
    }

    /**
     * Get the amount of items of the user
     *
     * @param user a user which contains a spaceship
     * @return
     */
    public long getItemCount(Account user) {
        return user.getSpaceship().getItemCount();
    }

    /**
     * Get items with name from the inventory of the user
     *
     * @param user a user which contains a spaceship
     * @param name the name to search
     * @return
     */
    public Collection<Item> getItemsWithName(Account user, String name) {
        return user.getSpaceship().findItem(name);
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
//        Account user = new Account(username, password1, new Spaceship(), new ArrayList<Resource>());
//        return createUser(user);
        System.out.println("CALLING CREATETEST");
        userDAO.createTest(new Test());
        return true;
    }

    public boolean login(String username, String password) {
        List<Account> users = userDAO.findAll();
        for (Account u : users) {
            System.out.println(u.getClass().getClassLoader().toString());
            System.out.println("USER: " + ((Account) u).getUsername());
            if (((Account) u).getUsername().equals(username)) {
                return ((Account) u).getPassword().equals(password);
            }
        }
        return false;
    }
}

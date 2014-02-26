package service;

import dao.UserDAO;
import domain.Item;
import domain.Resource;
import domain.Spaceship;
import domain.Stat;
import domain.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class StoreService {

    private UserDAO userDAO;

    private List<Item> mockupItems = new ArrayList<>();

    public StoreService() {
        initUsers();
        initItems();
    }

    private void initUsers() {
        User u1 = new User("Demo", "Demo", new Spaceship(), new ArrayList<Resource>());
        User u2 = new User("Demo2", "Demo", new Spaceship(), new ArrayList<Resource>());
        userDAO.create(u1);
        userDAO.create(u2);
    }

    private void initItems() {
        List<Resource> item1Resources = new ArrayList<>();
        item1Resources.add(new Resource("Mineral", 100));
        item1Resources.add(new Resource("Iron", 50));
        List<Stat> item1Stats = new ArrayList<>();
        item1Stats.add(new Stat("Stamina", 10));
        Item item1 = new Item(0, "Test1", "Lorem ipsum", item1Resources, item1Stats, false, "");
        Item item2 = new Item(1, "Test2", "Lorem ipsum", item1Resources, item1Stats, false, "");
        Item item3 = new Item(2, "Test3", "Lorem ipsum", item1Resources, item1Stats, false, "");

        this.mockupItems.add(item1);
        this.mockupItems.add(item2);
        this.mockupItems.add(item3);
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
    public List<User> getUsers() {
        return this.userDAO.findAll();
    }

    /**
     * Create a user
     *
     * @param user the user to create
     */
    public boolean createUser(User user) {
        for (User u : getUsers()) {
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
    public User getUser(String username) {
        return userDAO.find(username);
    }

    /**
     * Get all the items of a user
     *
     * @param user a user which contains a spaceship
     * @return
     */
    public Collection<Item> getItemsFromUser(User user) {
        return user.getSpaceship().getAllItems();
    }

    /**
     * Add item to inventory of user
     *
     * @param user a user which contains a spaceship
     * @param item the item to add
     */
    public void addItemToSpaceship(User user, Item item) {
        user.getSpaceship().addItemToInventory(item);
    }

    /**
     * Get the amount of items of the user
     *
     * @param user a user which contains a spaceship
     * @return
     */
    public long getItemCount(User user) {
        return user.getSpaceship().getItemCount();
    }

    /**
     * Get items with name from the inventory of the user
     *
     * @param user a user which contains a spaceship
     * @param name the name to search
     * @return
     */
    public Collection<Item> getItemsWithName(User user, String name) {
        return user.getSpaceship().findItem(name);
    }

    /**
     * Get items with name from the inventory of the user
     *
     * @param user a user which contains a spaceship
     * @param id the id to search
     * @return
     */
    public Item getItemWithId(User user, long id) {
        return user.getSpaceship().findItem(id);
    }

    //Comment van Lisanne
    public boolean registerUser(String username, String password1, String password2) {
        User user = new User(username, password1, new Spaceship(), new ArrayList<Resource>());
        return createUser(user);
    }

    //Comment van Jamy
    public boolean login(String username, String password) {
        for (User u : getUsers()) {
            if (u.getUsername().equals(username)) {
                return u.getPassword().equals(password);
            }
        }
        return false;
    }
}

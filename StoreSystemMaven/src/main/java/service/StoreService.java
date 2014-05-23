package service;

import annotations.AccountJPAImpl;
import annotations.ItemJPAImpl;
import dao.ItemDAO;
import dao.UserDAO;
import domain.Account;
import domain.Item;
import domain.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.xml.ws.WebServiceRef;
import webservice.StoreWebserviceService;

@Stateless
public class StoreService implements Serializable {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/192.168.24.11/StoreWebservice.wsdl")
    private StoreWebserviceService service;

    @Inject
    @AccountJPAImpl
    private UserDAO userDAO;

    @Inject
    @ItemJPAImpl
    private ItemDAO itemDAO;

    private Account loggedInAccount;

    Map<String, String> map = new HashMap<String, String>();

    public StoreService() {
    }

    public void addPayment(String guid, String paymentID) {
        this.map.put(guid, paymentID);
    }

    public String getPayment(String guid) {
        return this.map.get(guid);
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
//        user.addItemToSpaceShipInventory(item);
//        userDAO.edit(user);
        
        
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

    /**
     * Gets the current logged in user.
     *
     * @return the user that is logged in.
     */
    public Account getLoggedInAccount() {
        return this.loggedInAccount;
    }

    public boolean registerUser(String username, String password1, String password2) {
        boolean result = false;

        if (password1.equals(password2)) {
            try { // Call Web Service Operation
                webservice.StoreWebservice port = service.getStoreWebservicePort();
                // TODO initialize WS operation arguments here
                // TODO process result here
                result = port.registerUser(username, password1);
                System.out.println("Result = " + result);
            } catch (Exception ex) {
                ex.printStackTrace();
                // TODO handle custom exceptions here
            }
        }

        return result;

    }

    public boolean login(String username, String password) {
//
//        try { // Call Web Service Operation
//            webservice.Webservice port = service.getWebservicePort();
//            // TODO initialize WS operation arguments here
//            // TODO process result here
//            this.loggedInUsername = port.login(username, password);
//            System.out.println("Result = " + loggedInUsername);
//            return true;
//        } catch (Exception ex) {
//            // TODO handle custom exceptions here
//            ex.printStackTrace();
//        }
        return false;
    }

    public List<Item> getMyItems() {

//
//        try { // Call Web Service Operation
//            webservice.Webservice port = service.getWebservicePort();
//            // TODO initialize WS operation arguments here
//            webservice.Account arg0 = new webservice.Account();
//            // TODO process result here
//            return port.getResourcesForUser(arg0);
//        } catch (Exception ex) {
//            // TODO handle custom exceptions here
//            ex.printStackTrace();
//        }
        return null;
    }

//    public List<Item> getAllItems() {
//        return itemDAO.findAll();
//    }
    public boolean addResourceToLoggedInUser(Resource resource) {
        boolean result = false;
//        try { // Call Web Service Operation
//            webservice.Webservice port = service.getWebservicePort();
//            // TODO initialize WS operation arguments here
//            webservice.Resource _resource = new webservice.Resource();
//            _resource.setAmount(resource.getAmount());
//            _resource.setType(resource.getType());
//            _resource.setId(resource.getId());
//            port.addResourceToUser(_resource, this.loggedInAccount);
//            return true;
//        } catch (Exception ex) {
//            // TODO handle custom exceptions here
//            return false;
//        }
        if (result) {
            //not convenient to have resources in a collection. better split resources?
            for (Resource r : this.loggedInAccount.getResources()) {
                if (r.getType().equals("Common") && resource.getType().equals("Common")) {
                    r.setAmount(r.getAmount() + resource.getAmount());
                } else if (r.getType().equals("Magic") && resource.getType().equals("Magic")) {
                    r.setAmount(r.getAmount() + resource.getAmount());
                } else if (r.getType().equals("Rare") && resource.getType().equals("Rare")) {
                    r.setAmount(r.getAmount() + resource.getAmount());
                }
            }
        }
        return result;
    }

    public void buyItem(Item selectedItem) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.

        FacesContext context = FacesContext.getCurrentInstance();
        boolean result = false;

        //do webservice stuff
        if (result) {
            //substract money from user
            Collection<Resource> totalcost = selectedItem.getResources();
            Resource userCommmon = null, userMagic = null, userRare = null;
            for (Resource r : this.loggedInAccount.getResources()) {
                if (r.getType().equals("Common")) {
                    userCommmon = r;
                } else if (r.getType().equals("Magic")) {
                    userMagic = r;
                } else if (r.getType().equals("Rare")) {
                    userRare = r;
                }
            }
            for (Resource r : totalcost) {
                if (r.getType().equals("Common")) {
                    userCommmon.setAmount(userCommmon.getAmount() - r.getAmount());
                } else if (r.getType().equals("Magic")) {
                    userMagic.setAmount(userMagic.getAmount() - r.getAmount());
                } else if (r.getType().equals("Rare")) {
                    userRare.setAmount(userRare.getAmount() - r.getAmount());
                }
            }
        }
//        try { // Call Web Service Operation
//            webservice.Webservice port = service.getWebservicePort();
//            java.lang.Boolean result = port.buyItem(selectedItem.getId(), loggedInAccount.getId());
//
//            if (result) {
//                context.addMessage(null, new FacesMessage(selectedItem.getName() + " succesvol gekocht."));
//            } else {
//                context.addMessage(null, new FacesMessage("Onvoldoende resources."));
//            }
//            System.out.println("Result buyitem = " + result);
//        } catch (Exception ex) {
//            // TODO handle custom exceptions here
//        }
    }
}

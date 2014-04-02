package service;

import com.ptsesd.groepb.shared.AsteroidType;
import dao.ItemDAO;
import dao.ItemDAO_JPAImpl;
import domain.Item;
import domain.Resource;
import domain.Stat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EconomyService implements Serializable {

    private final ItemDAO itemDAO;
    private final AuctionHouse ah;

    public EconomyService() {
        this.itemDAO = new ItemDAO_JPAImpl();
        ah = new AuctionHouse();
        initItems();
    }

    public void closeEntityManager() {
        itemDAO.closeEntityManager();
    }

    private void initItems() {
        System.out.println("INIT");

        List<Resource> item1Resources = new ArrayList<>();
        item1Resources.add(new Resource("Common", 100));
        item1Resources.add(new Resource("Magic", 50));
        List<Stat> item1Stats = new ArrayList<>();
        item1Stats.add(new Stat("Stamina", 10));

        String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis hendrerit velit sit amet ligula faucibus, a aliquam dui consequat. Sed egestas mauris gravida sem commodo tristique. Maecenas sed mauris metus. Vestibulum nunc nunc, pretium et interdum eget, tincidunt ut eros. Suspendisse in urna nec enim cursus fermentum. Quisque ultricies eros laoreet, molestie odio vitae, congue nisl. Praesent eget eleifend lectus.";

        Item item1 = new Item("Item 1", lorem, item1Resources, item1Stats, "");
        Item item2 = new Item("Item 2", lorem, item1Resources, item1Stats, "");
        Item item3 = new Item("Item 3", lorem, item1Resources, item1Stats, "");
        Item item4 = new Item("Item 4", lorem, item1Resources, item1Stats, "");

        this.itemDAO.create(item1);
        this.itemDAO.create(item2);
        this.itemDAO.create(item3);
        this.itemDAO.create(item4);
    }

    /**
     * Get all items
     *
     * @return
     */
    public List<Item> getAllItems() {
        return itemDAO.findAll();
    }

//    public void buyItem(Item selectedItem) {
//        loggedInAccount = userDAO.find(loggedInAccount.getUsername());
//        FacesContext context = FacesContext.getCurrentInstance();
//
//        Boolean hasEnoughResources = true;
//
//        for (Resource ritem : selectedItem.getResources()) {
//            for (Resource raccount : loggedInAccount.getResources()) {
//                //if the type of the resources match ...
//                if (ritem.getType().equals(raccount.getType())) {
//                    if (raccount.getAmount() < ritem.getAmount()) {
//                        //if ammount is not enough. make boolean false
//                        hasEnoughResources = false;
//                    }
//                }
//            }
//        }
//
//        if (hasEnoughResources) {
//            for (Resource ritem : selectedItem.getResources()) {
//                for (Resource raccount : loggedInAccount.getResources()) {
//                    //if the type of the resources match ...
//                    if (ritem.getType().equals(raccount.getType())) {
//                        if (raccount.getAmount() >= ritem.getAmount()) {
//                            //reduce the resource of the player by the amount of resources of the item
//                            raccount.setAmount(raccount.getAmount() - ritem.getAmount());
//                        }
//                    }
//                }
//            }
//            
//            //add the item to the inventory of the currently loggedin user
//            loggedInAccount.addItemToSpaceShipInventory(selectedItem);
//            userDAO.edit(loggedInAccount);
//                
//            context.addMessage(null, new FacesMessage(selectedItem.getName() + " succesvol gekocht."));
//        } else {
//            context.addMessage(null, new FacesMessage("Onvoldoende resources."));
//        }
//
//    }
}

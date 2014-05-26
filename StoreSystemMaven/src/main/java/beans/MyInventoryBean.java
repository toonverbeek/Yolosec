/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import webservice.Item;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import service.StoreService;

/**
 *
 * @author Lisanne
 */
@Named(value = "myInventoryBean")
@RequestScoped
public class MyInventoryBean {

    @Inject
    private StoreService storeService;

    List<Item> myItems = new ArrayList<>();

    private Item selectedItem;

    public Item getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Item selectedItem) {
        this.selectedItem = selectedItem;
    }

    public MyInventoryBean() {
    }

    public List<Item> getMyItems() {
        return storeService.getMyItems();
    }

    public String goToMyInventory() {
        return "myInventoryContent.xhtml?faces-redirect=true";
    }
    
    public String goToStoreContent() {
        return "storeContent.xhtml?faces-redirect=true";
    }
}

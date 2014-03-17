/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import domain.Item;
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
@Named(value = "storeBean")
@RequestScoped
public class StoreBean {

    @Inject
    private StoreService storeService;

    List<Item> allItems = new ArrayList<>();

    private Item selectedItem;

    public Item getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Item selectedItem) {
        this.selectedItem = selectedItem;
    }

    public StoreBean() {
    }

    public List<Item> getAllItems() {
        return storeService.getAllItems();
    }

    public void setAllItems(List<Item> allItems) {
        this.allItems = allItems;
    }
    
    public void buyItem(){
        System.out.println("BUY");
        storeService.buyItem(selectedItem);
    }
    
}

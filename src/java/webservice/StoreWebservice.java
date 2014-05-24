/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import com.ptsesd.groepb.shared.Item;
import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.User;
import com.ptsesd.groepb.shared.UserItem;
import dao.ItemDAO;
import dao.ItemDAO_JPAImpl;
import dao.UserDAO;
import dao.UserDAO_Impl;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrator
 */
@WebService
public class StoreWebservice {
    
    UserDAO userDAO;
    ItemDAO itemDAO;
    
    public StoreWebservice(EntityManager em){
        userDAO = new UserDAO_Impl(em);
        itemDAO = new ItemDAO_JPAImpl(em);
    }

    public boolean registerUser(String username, String password) {
        System.out.println(String.format("--- [STOREWEBSERVICE] Got register user request User [%s] Password [%s]", new Object[]{username, password}));
        ArrayList<UserItem> inventory = new ArrayList<>();

        User user = new User(inventory, username, password);
        userDAO.registerUser(user);
        System.out.println("--- [STOREWEBSERVICE] Registerd user");
        return true;
    }
    
    public boolean login(String username, String password) {
        System.out.println(String.format("--- [STOREWEBSERVICE] Login request user [%s]" ,username));
        return userDAO.login(username, password);
    }
    
    public boolean buyItem(String username, long itemId) {
        System.out.println(String.format("--- [STOREWEBSERVICE] BuyItem request [%s]", username ));
        User user = userDAO.find(username);
        Item item = itemDAO.find(itemId);
        return itemDAO.buyItem(user, item);
    }
    
    public List<Item> getAllItems(){
        System.out.println("--- [STOREWEBSERVICE] GetAllItems request");
        List<Item> findAll = itemDAO.findAll();
        return findAll;
    }
    
    public boolean addResources(String username, float value, String resourceType){
        System.out.println(String.format("--- [STOREWEBSERVICE] AddResources request User [%s] || Value [%s] || ResourceType [%s]", new Object[]{username, value, resourceType} ));
        User find = userDAO.find(username);
        if(null != resourceType)switch (resourceType) {
            case "normal":
                int resource_normal = find.getResource_normal();
                if((resource_normal - value) > 0){
                    resource_normal += value;
                    return true;
                } else {
                    return false;
                }
            case "magic":
                int resource_magic = find.getResource_magic();
                if((resource_magic - value) > 0){
                    resource_magic += value;
                    return true;
                } else {
                    return false;
                }
            case "rare":
                int resource_rare = find.getResource_rare();
                if((resource_rare - value) > 0){
                    resource_rare += value;
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
        return false;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import service.StoreService;

/**
 *
 * @author Lisanne
 */
@Named(value="loginBean")
@RequestScoped
public class LoginBean {

    @Inject
    private StoreService storeService;

    private String username;
    private String password;

    public LoginBean() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        if (username != null && password != null) {
            if (storeService.login(username, password)) {
                System.out.println("------------StoreContent laden.....!!!");
                return "storeContent.xhtml";
            } else {
                return "";
            }
        }
        return "";
    }
}

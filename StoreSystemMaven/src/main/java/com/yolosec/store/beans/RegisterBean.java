/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.store.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.yolosec.store.service.StoreService;

/**
 *
 * @author Lisanne
 */
@Named(value = "registerBean")
@RequestScoped
public class RegisterBean {

    @Inject
    private StoreService storeService;

    private String username = "";
    private String password1 = "";
    private String password2 = "";

    public RegisterBean() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String register() {
        if (username != null && password1 != null && password2 != null) {
            if (storeService.registerUser(username, password1, password2)) {
                System.out.println("register!");
                return "loginContent.xhtml?faces-redirect=true";
            }else{
                return"";
            }
        }
        return "";
    }
}

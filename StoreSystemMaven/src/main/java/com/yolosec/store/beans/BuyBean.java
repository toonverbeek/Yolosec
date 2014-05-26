/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.store.beans;

import java.io.InputStream;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.yolosec.store.service.StoreService;
import com.paypal.api.payments.*;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;

/**
 *
 * @author Lisanne
 */
@Named(value = "buyBean")
@RequestScoped
public class BuyBean {

    @Inject
    private StoreService storeService;
    private Properties prop;
    private String accessToken;
    private String selectedAmount;

    private String order_amount;
    private String price;

    private Collection<String> urls = new ArrayList<>();

    @PostConstruct
    public void Init() {
        try {
            InputStream is = this.getClass().getResourceAsStream("/sdk_config.properties");
            prop = new Properties();
            prop.load(is);
        } catch (IOException ex) {
            Logger.getLogger(BuyBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getSelectedAmount() {
        return selectedAmount;
    }

    public void setSelectedAmount(String selectedAmount) {
        switch (selectedAmount) {
            case "1":
                order_amount = "1";
                price = "0.10";
                break;
            case "2":
                order_amount = "10";
                price = "0.90";
                break;
            case "3":
                order_amount = "100";
                price = "9.00";
                break;
            case "4":
                order_amount = "1000";
                price = "90.00";
                break;
        }
    }

    public BuyBean() {

    }

    public void buySpaceCoins() {
        String redirectURL = "";
        String responseURL = "";
        try {
            String clientID = prop.getProperty("clientID");//"AefQDhBgfgrfRps4kneKmZtdvEfjC-YsVWbTs5nVZGpyPHvSLlCwIEDI59s1";
            String clientSecret = prop.getProperty("clientSecret");//"EAFoeBBTNEcmJFiRb9OMFybh-TJLqqTlaZxjmbCenYSszF-aEwSkdGR_CKfT";
            APIContext context;
            accessToken = new OAuthTokenCredential(clientID, clientSecret).getAccessToken();
            context = new APIContext(accessToken);
            if (order_amount != null && price != null) {
                try {
                    //set the details of the payment
                    Details details = new Details();
                    details.setShipping("0");
                    details.setSubtotal(price);
                    details.setTax("0");

                    //set the amount of the payment
                    Amount amount = new Amount("EUR", price);
                    amount.setDetails(details);

                    //set itemlist
                    List<Item> items = new ArrayList<>();
                    double price2 = Double.valueOf(price);
                    double order_amount2 = Double.valueOf(order_amount);
                    String priceitem = "";
                    if (order_amount2 == 1) {
                        priceitem = price;
                    } else {
                        priceitem = price2 / order_amount2 + "";
                    }
                    items.add(new Item(order_amount, "SpaceCoin(s)", priceitem, "EUR"));

                    ItemList items2 = new ItemList();
                    items2.setItems(items);

                    //set the transaction
                    Transaction transaction = new Transaction();
                    transaction.setAmount(amount);
                    transaction.setDescription("You are about to buy SpaceCoins for the InSpace-game");
                    transaction.setItemList(items2);

                    //set the transaction
                    List<Transaction> transactions = new ArrayList<>();
                    transactions.add(transaction);

                    //set the payer
                    Payer payer = new Payer("paypal");

                    //set the payment
                    Payment payment = new Payment("sale", payer, transactions);

                    RedirectUrls redirectUrls = new RedirectUrls();
                    String guid = UUID.randomUUID().toString().replaceAll("-", "");
                    redirectUrls.setCancelUrl("http://localhost:8080/StoreSystemMaven/faces/buyPage.xhtml?guid=" + guid);
                    redirectUrls.setReturnUrl("http://localhost:8080/StoreSystemMaven/faces/confirmationPage.xhtml?guid=" + guid);
                    payment.setRedirectUrls(redirectUrls);
                    Payment createdPayment = payment.create(context);

                    System.out.println("Created payment with id = " + createdPayment.getId() + " and status = " + createdPayment.getState());
                    Iterator<Links> links = createdPayment.getLinks().iterator();
                    while (links.hasNext()) {
                        Links link = links.next();
                        if (link.getRel().equalsIgnoreCase("approval_url")) {
                            redirectURL = link.getHref();
                            urls.add(redirectURL);
                        }
                    }
                    responseURL = Payment.getLastResponse();
                    urls.add(responseURL);
                    storeService.addPayment(guid, createdPayment.getId());
                } catch (PayPalRESTException ex) {
                    Logger.getLogger(BuyBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect(redirectURL);
        } catch (PayPalRESTException | IOException ex) {
            Logger.getLogger(BuyBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Collection<String> getUrls() {
        return urls;
    }

}

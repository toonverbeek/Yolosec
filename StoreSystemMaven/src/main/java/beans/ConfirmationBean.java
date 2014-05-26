/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.paypal.api.payments.Item;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import service.StoreService;

/**
 *
 * @author Lisanne
 */
@Named(value = "confirmationBean")
@RequestScoped
public class ConfirmationBean {

    @Inject
    private StoreService storeService;

    private String guid;
    private String paymentID;
    
    private String itemname;
    private String amount;
    private String price;
    
    public ConfirmationBean() {
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) throws PayPalRESTException {
        try {
            this.guid = guid;
            this.paymentID = storeService.getPayment(guid);
            
            InputStream is = this.getClass().getResourceAsStream("/sdk_config.properties");
            Properties prop = new Properties();
            prop.load(is);
            String clientID = prop.getProperty("clientID");//"AefQDhBgfgrfRps4kneKmZtdvEfjC-YsVWbTs5nVZGpyPHvSLlCwIEDI59s1";
            String clientSecret = prop.getProperty("clientSecret");//"EAFoeBBTNEcmJFiRb9OMFybh-TJLqqTlaZxjmbCenYSszF-aEwSkdGR_CKfT";
            String accessToken = new OAuthTokenCredential(clientID, clientSecret).getAccessToken();
            Payment p = Payment.get(accessToken, paymentID);
            Transaction t = p.getTransactions().get(0);
            
            this.price = t.getAmount().getCurrency() + " " +  t.getAmount().getTotal();
            Item i = t.getItemList().getItems().get(0);
            this.itemname = i.getName();
            this.amount = i.getQuantity();
            storeService.addResourceToLoggedInUser(new domain.Resource("SpaceCoins", Long.valueOf(amount)));
        } catch (IOException ex) {
            Logger.getLogger(ConfirmationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getItemname() {
        return itemname;
    }

    public String getAmount() {
        return amount;
    }

    public String getPrice() {
        return price;
    }
}

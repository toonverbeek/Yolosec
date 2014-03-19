/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.InputStream;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import service.StoreService;
import com.paypal.api.payments.*;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lisanne
 */
@Named(value = "buyBean")
@RequestScoped
public class BuyBean {

    @Inject
    private StoreService storeService;

    @PostConstruct
    public void Init() {
        InputStream is = this.getClass().getResourceAsStream("/sdk_config.properties");
    }

    public BuyBean() {

    }

    public void buySpaceCoins() {
        String clientID = "AefQDhBgfgrfRps4kneKmZtdvEfjC-YsVWbTs5nVZGpyPHvSLlCwIEDI59s1";
        String clientSecret = "EAFoeBBTNEcmJFiRb9OMFybh-TJLqqTlaZxjmbCenYSszF-aEwSkdGR_CKfT";
        try {
            String accessToken = new OAuthTokenCredential(clientID, clientSecret).getAccessToken();
        } catch (PayPalRESTException ex) {
            Logger.getLogger(BuyBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

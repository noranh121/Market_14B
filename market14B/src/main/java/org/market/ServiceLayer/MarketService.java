package org.market.ServiceLayer;

import java.util.List;

import org.market.DomainLayer.backend.Market;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MarketService {

    @Autowired
    private Market market;// = Market.getInstance();
    //= Market.getInstance();

    // @Autowired
    // public MarketService(Market market){
    //     this.market = market;
    // }
    public String setMarketOnline(String username) throws Exception {
        try{
            return market.setMarketOnline(username);
        } catch(Exception exception){
            return exception.getMessage();
        }
        
    }

    public String setMarketOFFLINE(String username) throws Exception {
        try{
            return market.setMarketOFFLINE(username);
        } catch(Exception exception){
            return exception.getMessage();
        }
    }

    public List<String> getSystemManagers(){
        return market.getSystemManagers();
    }

    public void addToSystemManagers(String admin) {
        market.addToSystemManagers(admin);
    }

    public String addCatagory(int storeId, String catagory, String username) throws Exception{
        try {
            String result = market.addCatagory(storeId, catagory, username);
            Market.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String suspendUserIndefinitely(String systemManager, String username) {
        try {
            String result = market.suspendUser(systemManager, username);
            Market.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String suspendUserTemporarily(String systemManager,String username, int duration) {
        try {
            String result = market.suspendUserSeconds(systemManager, username,duration);
            Market.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String resumeUserIndefinitely(String systemManager, String username) {
        try {
            String result = market.resumeUser(systemManager, username);
            Market.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String resumeUserTemporarily(String systemManager,String username, int duration) {
        try {
            String result = market.resumeUserSeconds(systemManager, username,duration);
            Market.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String viewSuspended(String systemManager) {
        try {
            String result = market.viewSuspended(systemManager);
            Market.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String viewSystemPurchaseHistory(String username) {
        try{
            return market.viewSystemPurchaseHistory(username);
        }catch(Exception exception){
            return exception.getMessage();
        }
    }
}

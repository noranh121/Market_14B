package org.market.ServiceLayer;

import org.market.DomainLayer.backend.Market;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MarketService {
    private Market market; 
    //= Market.getInstance();

    @Autowired
    public MarketService(Market market){
        this.market = market;
    }
    public void setMarketOnline(String username) throws Exception {
        market.setMarketOnline(username);
    }

    public void setMarketOFFLINE(String username) throws Exception {
        market.setMarketOFFLINE(username);
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

    public void addToSystemManagers(String admin) {
        market.addToSystemManagers(admin);
    }
}

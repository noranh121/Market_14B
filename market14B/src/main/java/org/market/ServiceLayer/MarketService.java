package org.market.ServiceLayer;

import org.market.DomainLayer.backend.Market;
import org.market.Web.DTOS.ProductDTO;
import org.market.Web.Requests.SearchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MarketService {

    @Autowired
    private Market market;// = Market.getInstance();
    //= Market.getInstance();

    // @Autowired
    // public MarketService(Market market){
    //     this.market = market;
    // }
    public void setMarketOnline(String username) throws Exception {
        market.setMarketOnline(username);
    }

    public void setMarketOFFLINE(String username) throws Exception {
        market.setMarketOFFLINE(username);
    }

    public String addCatagory(int storeId, String catagory, String username) throws Exception{
            String result = market.addCatagory(storeId, catagory, username);
            Market.LOGGER.info(result);
            return result;
    }

    public String suspendUserIndefinitely(String systemManager, String username) throws Exception {
            String result = market.suspendUser(systemManager, username);
            Market.LOGGER.info(result);
            return result;
    }

    public String suspendUserTemporarily(String systemManager,String username, int duration) throws Exception{
            String result = market.suspendUserSeconds(systemManager, username,duration);
            Market.LOGGER.info(result);
            return result;
    }

    public String resumeUserIndefinitely(String systemManager, String username) throws Exception {
            String result = market.resumeUser(systemManager, username);
            Market.LOGGER.info(result);
            return result;
    }

    public String resumeUserTemporarily(String systemManager,String username, int duration) throws Exception {
            String result = market.resumeUserSeconds(systemManager, username,duration);
            Market.LOGGER.info(result);
            return result;
    }

    public String viewSuspended(String systemManager) throws Exception {
            String result = market.viewSuspended(systemManager);
            Market.LOGGER.info(result);
            return result;
    }

    public void addToSystemManagers(String admin) {
        market.addToSystemManagers(admin);
    }

    public boolean isSystemManager(String username) throws Exception {
        return market.isSystemManager(username);
    }

    public Integer getCategory(String name) {
        return market.getCategory(name);
    }

    public boolean hasCategory(String name) {
        return market.hasCategory(name);
    }

    public List<ProductDTO> search(SearchEntity entity){
        return this.market.search(entity);
    }
}

package org.market.ServiceLayer;

import java.time.LocalDate;
import java.util.List;

import org.market.DomainLayer.backend.Market;
import org.market.DomainLayer.backend.StorePackage.Purchase.PurchaseMethod;
import org.market.Web.DTOS.ProductDTO;
import org.market.Web.Requests.SearchEntity;
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

    public String viewSystemPurchaseHistory(String username) {
        try{
            return market.viewSystemPurchaseHistory(username);
        }catch(Exception exception){
            return exception.getMessage();
        }
    }

    public boolean isSystemManager(String username) throws Exception {
        return market.isSystemManager(username);
    }

    public List<ProductDTO> search(SearchEntity entity){
            return this.market.search(entity);
    }

    public Integer getCategory(String name) {
        return market.getCategory(name);
    }

    public boolean hasCategory(String name) {
        return market.hasCategory(name);
    }
}

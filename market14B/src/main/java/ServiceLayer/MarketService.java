package ServiceLayer;

import DomainLayer.backend.Market;

public class MarketService {
    private Market market = Market.getInstance();

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
}

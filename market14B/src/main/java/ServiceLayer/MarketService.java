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
}

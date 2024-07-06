package org.market.DomainLayer.backend.API.SupplyExternalService;

public class ProxySupply implements SupplyBridge {

    private SupplyBridge realSupply;

    public ProxySupply(){
        realSupply=new RealSupply();
    }

    @Override
    public String handshake() {
        return realSupply.handshake();
    }

    @Override
    public int supply(String name, String address, String city, String country, int zip) {
        return realSupply.supply(name, address, city, country, zip);
    }

    @Override
    public int cancel_supply(int transaction_id) {
        return realSupply.cancel_supply(transaction_id);
    }

}

package org.market.DomainLayer.backend.API.SupplyExternalService;

public interface SupplyBridge {

    String handshake();

    int supply(String name,String address,String city,String country,int zip);

    int cancel_supply(int transaction_id);

}

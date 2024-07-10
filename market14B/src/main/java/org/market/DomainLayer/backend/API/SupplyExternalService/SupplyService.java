package org.market.DomainLayer.backend.API.SupplyExternalService;

import org.market.ServiceLayer.Response;
import org.springframework.stereotype.Component;


@Component
public class SupplyService{

    private SupplyBridge proxySupply;
    private static SupplyService instance;

    private SupplyService(){
        proxySupply=new ProxySupply();
    }

    public static SupplyService getInstance(){
        if(instance==null){
            instance=new SupplyService();
        }
        return instance;
    }

    public Response<Boolean> handshake() {
        try{
            String message=proxySupply.handshake();
            if(message.equals("OK")){
                return Response.successRes(true);
            }
            else{
                return Response.failRes("handshake failed");
            }
        } catch (Exception exception){
            return Response.failRes("handshake failed");
        }
    }

    public Response<Integer> supply(String name, String address, String city, String country, int zip) {
        try{
            Integer trasnsaction_id=proxySupply.supply(name, address, city, country, zip);
            if(trasnsaction_id!=-1){
                return Response.successRes(trasnsaction_id);
            }
            else{
                return Response.failRes("supply failed");
            }
        } catch (Exception exception){
            return Response.failRes("supply failed");
        }
    }

    public Response<Integer> cancel_supply(int transaction_id) {
        try{
            int response=proxySupply.cancel_supply(transaction_id);
            if(response==1){
                return Response.successRes(response);
            }
            else{
                return Response.failRes("cancle supply failed");
            }
        } catch (Exception exception){
            return Response.failRes("cancle supply failed");
        }
    }
}

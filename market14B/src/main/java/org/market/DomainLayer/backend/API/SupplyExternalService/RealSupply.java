package org.market.DomainLayer.backend.API.SupplyExternalService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.client.RestTemplate;

import org.market.DomainLayer.backend.API.PostRequestService;

public class RealSupply implements SupplyBridge {

    private PostRequestService postRequestService;
    private String url;

    public RealSupply(){
        postRequestService=new PostRequestService(new RestTemplate());
        url="https://damp-lynna-wsep-1984852e.koyeb.app/";
    }

    @Override
    public String handshake() {
        Map<String,String> postContent=new ConcurrentHashMap<>();
        postContent.put("action_type", "handshake");
        return postRequestService.sendPostRequest(url, postContent);
    }

    @Override
    public int supply(String name, String address, String city, String country, int zip) {
        Map<String,String> postContent=new ConcurrentHashMap<>();
        postContent.put("action_type", "supply");
        postContent.put("name", name);
        postContent.put("address", address);
        postContent.put("city", city);
        postContent.put("country", country);
        postContent.put("zip", String.valueOf(zip));
        String response=postRequestService.sendPostRequest(url, postContent);
        try{
            return Integer.parseInt(response);
        }catch(Exception exception){
            return -1;
        }
    }

    @Override
    public int cancel_supply(int transaction_id) {
        Map<String,String> postContent=new ConcurrentHashMap<>();
        postContent.put("action_type", "cancel_supply");
        postContent.put("transaction_id", String.valueOf(transaction_id));
        String response=postRequestService.sendPostRequest(url, postContent);
        try{
            return Integer.parseInt(response);
        }catch(Exception exception){
            return -1;
        }
    }

}

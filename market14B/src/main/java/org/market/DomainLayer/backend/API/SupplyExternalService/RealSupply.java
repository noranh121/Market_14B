package org.market.DomainLayer.backend.API.SupplyExternalService;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.market.DomainLayer.backend.API.PostRequestService;

public class RealSupply implements SupplyBridge {

    private PostRequestService postRequestService;
    private String url="https://damp-lynna-wsep-1984852e.koyeb.app/";

    public RealSupply(){
        postRequestService=new PostRequestService();
    }

    @Override
    public String handshake() {
        MultiValueMap<String, String> postContent = new LinkedMultiValueMap<>();
        postContent.add("action_type", "handshake");
        return postRequestService.sendPostRequest(url, postContent);
    }

    @Override
    public int supply(String name, String address, String city, String country, String zip) {
        MultiValueMap<String, String> postContent = new LinkedMultiValueMap<>();
        postContent.add("action_type", "supply");
        postContent.add("name", name);
        postContent.add("address", address);
        postContent.add("city", city);
        postContent.add("country", country);
        postContent.add("zip", zip);
        String response=postRequestService.sendPostRequest(url, postContent);
        try{
            return Integer.parseInt(response);
        }catch(Exception exception){
            return -1;
        }
    }

    @Override
    public int cancel_supply(int transaction_id) {
        MultiValueMap<String, String> postContent = new LinkedMultiValueMap<>();
        postContent.add("action_type", "cancel_supply");
        postContent.add("transaction_id", String.valueOf(transaction_id));
        String response=postRequestService.sendPostRequest(url, postContent);
        try{
            return Integer.parseInt(response);
        }catch(Exception exception){
            return -1;
        }
    }


//     public static void main(String[] args) {
//        RealSupply supplyService = new RealSupply();
//        String handshake=supplyService.handshake();
//        int supply=supplyService.supply("Israel Israelovice","Rager Blvd 12","Beer Sheva","Israel","8458527");
//        Integer cancellationResult = supplyService.cancel_supply(1234);
//        System.out.println("supply: " + supply);
//        System.out.println("handshake: " + handshake);
//        System.out.println("cancel: " + cancellationResult);
//    }


}

package org.market.DomainLayer.backend.API.PaymentExternalService;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.market.DomainLayer.backend.API.PostRequestService;

public class RealPayment implements PaymentBridge {

    private PostRequestService postRequestService;
    private String url="https://damp-lynna-wsep-1984852e.koyeb.app/";

    public RealPayment(){
        postRequestService=new PostRequestService();
    }

    @Override
    public String handshake() {
        MultiValueMap<String, String> postContent = new LinkedMultiValueMap<>();
        postContent.add("action_type", "handshake");
        return postRequestService.sendPostRequest(url, postContent);
    }

    @Override
    public int pay(String amount, String currency, String card_number, String month, String year, String holder, String ccv, String id) {
        MultiValueMap<String, String> postContent = new LinkedMultiValueMap<>();
        postContent.add("action_type", "pay");
        postContent.add("amount", amount);
        postContent.add("currency", currency);
        postContent.add("card_number", card_number);
        postContent.add("month", month);
        postContent.add("year", year);
        postContent.add("holder", holder);
        postContent.add("ccv", ccv);
        postContent.add("id", id);
        String response=postRequestService.sendPostRequest(url, postContent);
        try{
            return Integer.parseInt(response);
        }catch(Exception exception){
            return -1;
        }
    }

    @Override
    public int cancel_pay(int transaction_id) {
        MultiValueMap<String, String> postContent = new LinkedMultiValueMap<>();
        postContent.add("action_type", "cancel_pay");
        postContent.add("transaction_id", String.valueOf(transaction_id));
        String response=postRequestService.sendPostRequest(url, postContent);
        try{
            return Integer.parseInt(response);
        }catch(Exception exception){
            return -1;
        }
    }

    // public static void main(String[] args) {
    //     RealPayment payment = new RealPayment();
    //     String handshake=payment.handshake();
    //     Integer pay=payment.pay("1000", "USD", "2222333344445555", "4", "2021", "Israel Israelovice","262","20444444");
    //     Integer cancellationResult = payment.cancel_pay(1234);
    //     System.out.println("handshake: " + handshake);
    //     System.out.println("pay: " + pay);
    //     System.out.println("cancel: " + cancellationResult);
    // }
}

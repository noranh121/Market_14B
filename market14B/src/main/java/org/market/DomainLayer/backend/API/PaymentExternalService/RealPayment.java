package org.market.DomainLayer.backend.API.PaymentExternalService;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.market.DomainLayer.backend.API.PostRequestService;

public class RealPayment implements PaymentBridge {

    private PostRequestService postRequestService;
    private String url="https://damp-lynna-wsep-1984852e.koyeb.app/";

    public RealPayment(){
        postRequestService=new PostRequestService();
        // loadConfig();
    }

    public void loadConfig() {
        Properties properties = new Properties();
        try {
            File configFile = new File("market14B\\src\\main\\java\\org\\market\\DomainLaye\\backend\\API\\config.properties");
            FileInputStream fileInputStream = new FileInputStream(configFile);
            properties.load(fileInputStream);
            url = properties.getProperty("url");
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while loading configuration from config.properties file.", e);
        }
    }

    @Override
    public String handshake() {
        MultiValueMap<String, String> postContent = new LinkedMultiValueMap<>();
        postContent.add("action_type", "handshake");
        return postRequestService.sendPostRequest(url, postContent);
    }

    @Override
    public int pay(double amount, String currency, String card_number, int month, int year, String holder, String ccv) {
        MultiValueMap<String, String> postContent = new LinkedMultiValueMap<>();
        postContent.add("action_type", "pay");
        postContent.add("amount", String.valueOf(amount));
        postContent.add("currency", currency);
        postContent.add("card_number", card_number);
        postContent.add("month", String.valueOf(month));
        postContent.add("year", String.valueOf(year));
        postContent.add("holder", holder);
        postContent.add("ccv", ccv);
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
    //     RealPayment supply = new RealPayment();
    //     String handshake=supply.handshake();
    //     Integer cancellationResult = supply.cancel_pay(1234);
    //     System.out.println("handshake: " + handshake);
    //     System.out.println("cancel: " + cancellationResult);
    // }
}

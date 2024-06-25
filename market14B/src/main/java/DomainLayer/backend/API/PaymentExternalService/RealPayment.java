package DomainLayer.backend.API.PaymentExternalService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.client.RestTemplate;

import DomainLayer.backend.API.PostRequestService;

public class RealPayment implements PaymentBridge {

    private PostRequestService postRequestService;
    private String url;

    public RealPayment(){
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
    public int pay(double amount, String currency, String card_number, int month, int year, String holder, String ccv) {
        Map<String,String> postContent=new ConcurrentHashMap<>();
        postContent.put("action_type", "pay");
        postContent.put("amount", String.valueOf(amount));
        postContent.put("currency", currency);
        postContent.put("card_number", card_number);
        postContent.put("month", String.valueOf(month));
        postContent.put("year", String.valueOf(year));
        postContent.put("holder", holder);
        postContent.put("ccv", ccv);
        String response=postRequestService.sendPostRequest(url, postContent);
        try{
            return Integer.parseInt(response);
        }catch(Exception exception){
            return -1;
        }
    }

    @Override
    public int cancel_pay(int transaction_id) {
        Map<String,String> postContent=new ConcurrentHashMap<>();
        postContent.put("action_type", "cancel_pay");
        postContent.put("transaction_id", String.valueOf(transaction_id));
        String response=postRequestService.sendPostRequest(url, postContent);
        try{
            return Integer.parseInt(response);
        }catch(Exception exception){
            return -1;
        }
    }

}

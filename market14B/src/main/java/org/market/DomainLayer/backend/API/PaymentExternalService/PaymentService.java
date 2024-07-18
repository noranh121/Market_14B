package org.market.DomainLayer.backend.API.PaymentExternalService;

import org.market.ServiceLayer.Response;
import org.springframework.stereotype.Component;

@Component
public class PaymentService {

    private PaymentBridge proxyPayment;
    private static PaymentService instance;

    private PaymentService(){
        proxyPayment=new ProxyPayment();
    }

    public static PaymentService getInstance(){
        if(instance==null){
            instance=new PaymentService();
        }
        return instance;
    }

    
    public Response<Boolean> handshake() {
        try{
            String message=proxyPayment.handshake();
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

    public Response<Integer> pay(String amount,String currency,String card_number,String month,String year,String holder,String ccv,String id) {
        try{
            Integer trasnsaction_id=proxyPayment.pay(amount, currency, card_number, month, year, holder, ccv,id);
            if(trasnsaction_id!=-1){
                return Response.successRes(trasnsaction_id);
            }
            else{
                return Response.failRes("pay failed");
            }
        } catch (Exception exception){
            return Response.failRes("pay failed");
        }
    }

    public Response<Integer> cancel_pay(Integer transaction_id) {
        try{
            int response=proxyPayment.cancel_pay(transaction_id);
            if(response==1){
                return Response.successRes(response);
            }
            else{
                return Response.failRes("cancle pay failed");
            }
        } catch (Exception exception){
            return Response.failRes("cancle pay failed");
        }
    }

}

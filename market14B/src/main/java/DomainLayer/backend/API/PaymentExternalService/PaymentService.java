package DomainLayer.backend.API.PaymentExternalService;

import ServiceLayer.Response;

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

    public Response<Integer> pay(double amount,String currency,String card_number,int month,int year,String holder,String ccv) {
        try{
            Integer trasnsaction_id=proxyPayment.pay(amount, currency, card_number, month, year, holder, ccv);
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

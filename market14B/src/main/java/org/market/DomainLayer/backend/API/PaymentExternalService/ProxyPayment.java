package org.market.DomainLayer.backend.API.PaymentExternalService;

public class ProxyPayment implements PaymentBridge{
    
    private PaymentBridge realPayment;

    public ProxyPayment(){
        realPayment=new RealPayment();
    }

    @Override
    public String handshake() {
        return realPayment.handshake();
    }

    @Override
    public int pay(String amount, String currency, String card_number, String month, String year, String holder, String ccv,String id) {
        return realPayment.pay(amount, currency, card_number, month, year, holder, ccv,id);
    }

    @Override
    public int cancel_pay(int transaction_id) {
        return realPayment.cancel_pay(transaction_id);
    }

}

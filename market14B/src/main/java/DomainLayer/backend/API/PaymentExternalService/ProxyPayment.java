package DomainLayer.backend.API.PaymentExternalService;

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
    public int pay(double amount, String currency, String card_number, int month, int year, String holder, String ccv) {
        return realPayment.pay(amount, currency, card_number, month, year, holder, ccv);
    }

    @Override
    public int cancel_pay(int transaction_id) {
        return realPayment.cancel_pay(transaction_id);
    }

}

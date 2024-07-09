package org.market.DomainLayer.backend.API.PaymentExternalService;

public interface PaymentBridge {

    String handshake();

    int pay(double amount,String currency,String card_number,int month,int year,String holder,String ccv);

    int cancel_pay(int transaction_id);

}
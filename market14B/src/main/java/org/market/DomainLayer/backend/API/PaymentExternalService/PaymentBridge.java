package org.market.DomainLayer.backend.API.PaymentExternalService;

public interface PaymentBridge {

    String handshake();

    int pay(String amount, String currency, String card_number, String month, String year, String holder, String ccv, String id);

    int cancel_pay(int transaction_id);

}

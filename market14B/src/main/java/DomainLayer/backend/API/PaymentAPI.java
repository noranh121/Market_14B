package DomainLayer.backend.API;

public interface PaymentAPI {
    String processCreditCardPayment(String cardNumber, String cardHolderName, String cvv, String expirationDateString, double amount);
    String processPaypalPayment(String paypalAccount, double amount);
    String processApplePayPayment(String applePayToken, double amount);
    String processGooglePayPayment(String googlePayToken, double amount);
}

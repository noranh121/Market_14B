package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import org.market.PresentationLayer.handlers.ErrorHandler;
import org.market.PresentationLayer.views.PaymentView;
import org.market.Web.Requests.PaymentReq;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class PaymentPresenter {

    private PaymentView view;
    private RestTemplate restTemplate;

    public PaymentPresenter(PaymentView paymentView){
        this.view = paymentView;
        this.restTemplate = new RestTemplate();
        initView();

    }

    private void initView() {
        this.view.setSubmitClickEventListener(e -> onSubmitButtonClicked());
    }

    private void onSubmitButtonClicked(){
        if(validatePaymentSubmit(view.getUsername(),view.getCurrency(),view.getCardNumber(),view.getMonth(),view.getYear(),
                view.getCcv(), view.getAddress(),view.getCity(),view.getCountry(),view.getZip())) {
            try {
                String url = "http://localhost:8080/api/users/buy";

                PaymentReq request = new PaymentReq();
                request.setUsername(view.getUsername().getValue());
                request.setCurrency(view.getCurrency().getValue());
                request.setCard(view.getCardNumber().getValue());
                request.setMonth(view.getMonth().getValue());
                request.setYear(view.getYear().getValue());
                request.setCcv(view.getCcv().getValue());
                request.setAddress(view.getAddress().getValue());
                request.setCity(view.getCity().getValue());
                request.setCity(view.getCity().getValue());
                request.setCountry(view.getCountry().getValue());
                request.setZip(view.getZip().getValue());


                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<PaymentReq> requestEntity = new HttpEntity<>(request, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                ErrorHandler.showSuccessNotification("Successfully purchased the cart");

            } catch (HttpClientErrorException e) {
                ErrorHandler.handleError(e, () -> {
                });
            }
        }
    }

    private boolean validatePaymentSubmit(TextField username, ComboBox<String> currency, TextField cardNumber,
                                          ComboBox<Integer> month,ComboBox<Integer> year,TextField ccv, TextField address
    ,TextField city, ComboBox<String> country, IntegerField zip) {
        boolean isValid = true;

        if (username.isEmpty()) {
            username.setInvalid(true);
            username.setErrorMessage("Username is required");
            isValid = false;
        }
        if (currency.isEmpty()) {
            currency.setInvalid(true);
            currency.setErrorMessage("Currency is required");
            isValid = false;
        }
        if (cardNumber.isEmpty()) {
            cardNumber.setInvalid(true);
            cardNumber.setErrorMessage("CardNumber is required");
            isValid = false;
        }
        if (month.isEmpty()) {
            month.setInvalid(true);
            month.setErrorMessage("Month is required");
            isValid = false;
        }
        if (year.isEmpty()) {
            year.setInvalid(true);
            year.setErrorMessage("Year is required");
            isValid = false;
        }
        if (ccv.isEmpty()) {
            ccv.setInvalid(true);
            ccv.setErrorMessage("Ccv is required");
            isValid = false;
        }
        if (address.isEmpty()) {
            address.setInvalid(true);
            address.setErrorMessage("Address is required");
            isValid = false;
        }
        if (city.isEmpty()) {
            city.setInvalid(true);
            city.setErrorMessage("City is required");
            isValid = false;
        }
        if (country.isEmpty()) {
            country.setInvalid(true);
            country.setErrorMessage("Country is required");
            isValid = false;
        }
        if (zip.isEmpty()) {
            zip.setInvalid(true);
            zip.setErrorMessage("Zip is required");
            isValid = false;
        }
        return isValid;
    }
}

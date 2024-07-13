package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.handlers.ErrorHandler;
import org.market.PresentationLayer.views.PurchaseHistoryView;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class PurchaseHistoryPresenter {

    private PurchaseHistoryView view;
    private RestTemplate restTemplate;

    public PurchaseHistoryPresenter(PurchaseHistoryView purchaseHistoryView) {
        this.view = purchaseHistoryView;
        this.restTemplate = new RestTemplate();
        initView();
    }

    public void initView() {
        this.view.setDeleteClickEventListener(e -> {
            onDeleteButtonClick(view.getDeleteField());
        });
        loadPurchaseHistory();
    }

    private void onDeleteButtonClick(IntegerField deleteField) {
        if (validateDeleteField(deleteField)) {
            try {
                String deleteUrl = "http://localhost:8080/api/purchases/delete-purchase";
                String username = (String) VaadinSession.getCurrent().getAttribute("current-user");

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Integer> requestEntity = new HttpEntity<>(deleteField.getValue(), headers);

                ResponseEntity<String> response = restTemplate.exchange(deleteUrl, HttpMethod.POST, requestEntity, String.class);

                List<String> toresponse = new ArrayList<>();
                String toenter = response.getBody();
                toresponse.add(toenter);
                view.createPurchaseHistoryLayout(toresponse);

                ErrorHandler.showSuccessNotification("Successfully deleted purchase at index " + deleteField.getValue());

            } catch (HttpClientErrorException e) {
                ErrorHandler.handleError(e, () -> {
                });
            }
        }
    }

    private boolean validateDeleteField(IntegerField deleteField) {
        boolean isValid = true;

        if (deleteField.isEmpty()) {
            deleteField.setInvalid(true);
            deleteField.setErrorMessage("Index is required");
            isValid = false;
        }
        return isValid;
    }

    public void loadPurchaseHistory() {
        try {
            String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
            String url = "http://localhost:8080/api/users/purchase-history/{username}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<String>>() {}, username);

            view.createPurchaseHistoryLayout(response.getBody() != null ? response.getBody() : new ArrayList<>());

        } catch (HttpClientErrorException e) {
            ErrorHandler.handleError(e, () -> {
            });
        }
    }

//    public void loadPurchases() {
//        // Example purchases, replace with actual backend call
//        purchaseList.add("Purchase 1: Details for purchase 1");
//        purchaseList.add("Purchase 2: Details for purchase 2");
//
//        view.createPurchasesLayout(purchaseList);
//    }
//
//    public void removePurchase(Integer id) {
//        if (id != null && id >= 0 && id < purchaseList.size()) {
//            purchaseList.remove(id.intValue());
//            view.createPurchasesLayout(purchaseList);
//        }
//    }

}

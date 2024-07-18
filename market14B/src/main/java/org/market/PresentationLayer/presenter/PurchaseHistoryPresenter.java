package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.handlers.ErrorHandler;
import org.market.PresentationLayer.views.components.PurchaseHistory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class PurchaseHistoryPresenter {

    private PurchaseHistory view;
    private RestTemplate restTemplate;

    public PurchaseHistoryPresenter(PurchaseHistory purchaseHistoryView) {
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
                String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
                String storeUrl = "http://localhost:8080/api/stores/remove-purchase/store={store_id}&purchase={purchase_id}";
                String userUrl = "http://localhost:8080/api/users/remove-purchase/user={username}&purchase={purchase_id}";
                String url = view.getStore_id() != -1 ? storeUrl : userUrl;

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Integer> requestEntity = new HttpEntity<>(deleteField.getValue(), headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class,
                        view.getStore_id() != -1 ? view.getStore_id() : username, deleteField.getValue());

                ErrorHandler.showSuccessNotification("Successfully deleted purchase with id = " + deleteField.getValue());

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
            isValid = false;
        }
        return isValid;
    }

    public void loadPurchaseHistory() {
        try {
            String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
            String userUrl = "http://localhost:8080/api/users/purchase-history/{username}";
            String storeUrl = "http://localhost:8080/api/stores/purchase-history/{store_id}";
            String url = view.getStore_id() != -1 ? storeUrl : userUrl;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<String>>() {}, view.getStore_id() != -1 ? view.getStore_id() : username);

            view.createPurchaseHistoryLayout(response.getBody() != null ? response.getBody() : new ArrayList<>());

        } catch (HttpClientErrorException e) {
            ErrorHandler.handleError(e, () -> {
            });
        }
    }

}

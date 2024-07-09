package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.errors.ErrorHandler;
import org.market.PresentationLayer.views.StoreView;
import org.market.Web.DTOS.StoreDTO;
import org.market.Web.Requests.AddProductReq;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

public class StorePresenter {

    private StoreView view;
    private RestTemplate restTemplate;

    public StorePresenter(StoreView storeView){
        this.view = storeView;
        this.restTemplate = new RestTemplate();
        initView();
    }

    private void initView() {
        System.out.println("id: sssss " + view.getStore_id());
        StoreDTO store = getStore(this.view.getStore_id());
        this.view.setStoreLayout(store);
        this.view.setApplyButtonClickEventListener(e ->
                onApplyAddProductClicked(view.getName_field(), view.getDescription_field(),
                        view.getPrice_field(), view.getWeight_field(), view.getInventory_field())
        );
    }

    private boolean validateAddForm(TextField name_field, TextField description_field, NumberField price_field, NumberField weight_field, IntegerField inventory_field) {
        boolean isValid = true;

        if (name_field.isEmpty()) {
            name_field.setInvalid(true);
            name_field.setErrorMessage("Name is required");
            isValid = false;
        }
        if (description_field.isEmpty()) {
            description_field.setInvalid(true);
            description_field.setErrorMessage("Description is required");
            isValid = false;
        }
        if (price_field.isEmpty()) {
            price_field.setInvalid(true);
            price_field.setErrorMessage("Price is required");
            isValid = false;
        }

        if (weight_field.isEmpty()) {
            weight_field.setInvalid(true);
            weight_field.setErrorMessage("Weight is required");
            isValid = false;
        }
        if (inventory_field.isEmpty()) {
            inventory_field.setInvalid(true);
            inventory_field.setErrorMessage("Inventory is required");
            isValid = false;
        }

        return isValid;
    }

    private StoreDTO getStore(int store_id) {
        try{
            String getStoreUrl = "http://localhost:8080/api/stores/store/{store_id}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<StoreDTO> response = restTemplate.exchange(getStoreUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<StoreDTO>() {}, store_id);

            return response.getBody();

        }catch (HttpClientErrorException e){
            ErrorHandler.handleError(e, ()->{});
            return null;
        }
    }

    private void onApplyAddProductClicked(TextField name_field, TextField description_field, NumberField price_field, NumberField weight_field, IntegerField inventory_field) {
        Runnable originalRequest = new Runnable() {
            @Override
            public void run() {
                try {
                    if (validateAddForm(name_field, description_field, price_field, weight_field, inventory_field)) {

                        String accessToken = (String) VaadinSession.getCurrent().getAttribute("access-token");
                        String username = (String) VaadinSession.getCurrent().getAttribute("current-user");

                        Random random = new Random();
                        int id = random.nextInt();

                        String addProductUrl = "http://localhost:8080/api/stores/add-product";

                        AddProductReq request = new AddProductReq();
                        request.setName(name_field.getValue());
                        request.setDescription(description_field.getValue());
                        request.setPrice(price_field.getValue());
                        request.setWeight(weight_field.getValue());
                        request.setInventory(inventory_field.getValue());
                        request.setId(id);
                        request.setUsername(username);
                        request.setStore_id(view.getStore_id());

                        HttpHeaders headers = new HttpHeaders();
                        headers.setBearerAuth(accessToken);
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        HttpEntity<AddProductReq> requestEntity = new HttpEntity<>(request,headers);

                        ResponseEntity<String> response = restTemplate.exchange(addProductUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<String>() {});

                        ErrorHandler.showSuccessNotification(response.getBody());

                    }
                } catch (HttpClientErrorException e) {
                    ErrorHandler.handleError(e, this);
                }
            }
        };

        originalRequest.run();
    }
}

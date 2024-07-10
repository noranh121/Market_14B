package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.handlers.ErrorHandler;
import org.market.PresentationLayer.handlers.PermissionHandler;
import org.market.PresentationLayer.views.MyStoresView;
import org.market.Web.DTOS.StoreDTO;
import org.market.Web.Requests.AddStoreReq;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class MyStoresPresenter {

    private MyStoresView view;
    private RestTemplate restTemplate;

    public MyStoresPresenter(MyStoresView myStoresView){
        this.view = myStoresView;
        this.restTemplate = new RestTemplate();
        initView();
    }

    public void initView(){
        this.view.setApplyButtonClickEventListener(e -> {
            onApplyButtonClicked(view.getName_field(), view.getDescription_field());
            loadStores();
        });
    }

    public void onApplyButtonClicked(TextField name_field, TextField description_field) {
        Runnable originalRequest = new Runnable() {
            @Override
            public void run() {
                try {
                    if (validateAddForm(name_field, description_field)) {
                        System.out.println("run");

                        String accessToken = (String) VaadinSession.getCurrent().getAttribute("access-token");
                        String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
                        String addStoreUrl = "http://localhost:8080/api/stores/add-store";

                        AddStoreReq request = new AddStoreReq();
                        request.setName(name_field.getValue());
                        request.setDescription(description_field.getValue());
                        request.setUsername(username);

                        HttpHeaders headers = new HttpHeaders();
                        headers.setBearerAuth(accessToken);
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        HttpEntity<AddStoreReq> requestEntity = new HttpEntity<>(request, headers);

                        ResponseEntity<String> response = restTemplate.exchange(addStoreUrl, HttpMethod.POST, requestEntity, String.class);

                        ErrorHandler.showSuccessNotification(response.getBody());

                        PermissionHandler.loadPermissions(username);

                    }
                } catch(HttpClientErrorException e){
                    ErrorHandler.handleError(e, this);
                }
            }
        };

        // Execute the original request
        originalRequest.run();
    }

    private ArrayList<StoreDTO> getMyStores() {
        ArrayList<StoreDTO> result = new ArrayList<>();
        try{
            String username = (String) VaadinSession.getCurrent().getAttribute("current-user");

            String storesUrl = "http://localhost:8080/api/stores/mystores/{username}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<ArrayList<StoreDTO>> response = restTemplate.exchange(storesUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<ArrayList<StoreDTO>>() {}, username);

            result = response.getBody();

        }catch (HttpClientErrorException e){
            ErrorHandler.handleError(e, ()->{});
        }
        return result;
    }

    private boolean validateAddForm(TextField name_field, TextField description_field) {
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

        return isValid;
    }

    public void loadStores() {
        List<StoreDTO> stores = getMyStores();
        this.view.loadMyStores(stores);
    }
}

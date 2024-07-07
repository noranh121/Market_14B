package org.market.PresentationLayer.presenter;

import org.market.PresentationLayer.errors.ErrorHandler;
import org.market.PresentationLayer.views.components.StoreCollection;
import org.market.Web.DTOS.StoreDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class StoreCollectionPresenter {

    private StoreCollection view;
    private RestTemplate restTemplate;


    public StoreCollectionPresenter(StoreCollection stores){
        this.view = stores;
        this.restTemplate = new RestTemplate();
        initView();
    }

    private void initView() {
        ArrayList<StoreDTO> stores = getAllStores();
        this.view.loadStores(stores);
    }

    private ArrayList<StoreDTO> getAllStores() {
        ArrayList<StoreDTO> result = new ArrayList<>();
        try{
            String storesUrl = "http://localhost:8080/api/stores/all";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<ArrayList<StoreDTO>> response = restTemplate.exchange(storesUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<ArrayList<StoreDTO>>() {});

            result = response.getBody();

        }catch (HttpClientErrorException e){
            ErrorHandler.handleError(e, ()->{});
        }
        return result;
    }


}

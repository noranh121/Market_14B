package org.market.PresentationLayer.presenter;

import org.market.PresentationLayer.handlers.ErrorHandler;
import org.market.PresentationLayer.views.components.ProductCollection;
import org.market.Web.DTOS.ProductDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class ProductCollectionPresenter {

    private ProductCollection view;
    private RestTemplate restTemplate;
    private int store_id = -1;


    public ProductCollectionPresenter(ProductCollection products, int store_id){
         this.view = products;
         this.store_id = store_id;
         this.restTemplate = new RestTemplate();
         initView();
    }

    private void initView() {
        ArrayList<ProductDTO> products;
        if(store_id == -1) {
            products = getAllProducts();
        }
        else{
            products = getStoreProducts();
        }
        this.view.loadProducts(products);
    }

    private ArrayList<ProductDTO> getStoreProducts() {
        ArrayList<ProductDTO> result = new ArrayList<>();
        try{
            String productsUrl = "http://localhost:8080/api/stores/products/{store_id}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<ArrayList<ProductDTO>> response = restTemplate.exchange(productsUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<ArrayList<ProductDTO>>() {}, store_id);

            result = response.getBody();

        }catch (HttpClientErrorException e){
            ErrorHandler.handleError(e, ()->{});
        }
        return result;
    }

    private ArrayList<ProductDTO> getAllProducts() {
        ArrayList<ProductDTO> result = new ArrayList<>();
        try{
            String productsUrl = "http://localhost:8080/api/stores/products/all";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<ArrayList<ProductDTO>> response = restTemplate.exchange(productsUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<ArrayList<ProductDTO>>() {});

            result = response.getBody();

        }catch (HttpClientErrorException e){
            ErrorHandler.handleError(e, ()->{});
        }
        return result;
    }


}

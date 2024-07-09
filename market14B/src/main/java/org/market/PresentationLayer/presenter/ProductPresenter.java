package org.market.PresentationLayer.presenter;

import org.market.PresentationLayer.errors.ErrorHandler;
import org.market.PresentationLayer.views.ProductView;
import org.market.Web.DTOS.ProductDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ProductPresenter {

    private ProductView view;
    private RestTemplate restTemplate;

    public ProductPresenter(ProductView productView){
        this.view = productView;
        this.restTemplate = new RestTemplate();
        initView();
    }

    private void initView() {
        ProductDTO product = getProduct(this.view.getProduct_id());
        this.view.setTopLayout(product);
    }

    private ProductDTO getProduct(int product_id) {
        try{
            String getProductUrl = "http://localhost:8080/api/stores/product/{product_id}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<ProductDTO> response = restTemplate.exchange(getProductUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<ProductDTO>() {}, product_id);

            return response.getBody();

        }catch (HttpClientErrorException e){
            ErrorHandler.handleError(e, ()->{});
            return null;
        }
    }
}

package org.market.PresentationLayer.presenter;

import org.market.PresentationLayer.views.components.ProductCollection;
import org.market.Web.DTOS.ProductDTO;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class ProductCollectionPresenter {

    private ProductCollection view;
    private RestTemplate restTemplate;


    public ProductCollectionPresenter(ProductCollection products, String store_id){
         this.view = products;
            this.restTemplate = new RestTemplate();
            initView();
        }

    private void initView() {
        ArrayList<ProductDTO> products = new ArrayList<>(); // INIT BASED ON RESULT OF API CALL
        this.view.loadProducts(products);
    }


}

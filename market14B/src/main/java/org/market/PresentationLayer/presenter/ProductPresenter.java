package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.handlers.ErrorHandler;
import org.market.PresentationLayer.views.ProductView;
import org.market.Web.DTOS.ProductDTO;
import org.market.Web.Requests.ReqStore;
import org.market.Web.Requests.cartOp;
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
        this.view.setSpecificationLayout(product);
        this.view.setSaveButtonClickEventListener(e ->{
            if(!view.getNew_price_field().isEmpty()){
                onUpdatePrice(view.getNew_price_field());
            }
            if(!view.getNew_inventory_field().isEmpty()){
                onUpdateInventory(view.getNew_inventory_field());
            }
            view.setEditLayoutVisible(false);
            if(!view.getNew_inventory_field().isEmpty() || !view.getNew_price_field().isEmpty()){
                UI.getCurrent().getPage().reload();
            }
        });
        this.view.setAddToCartButtonClickEventListener(e-> onAddToCart());
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

    private void onUpdatePrice(NumberField newPriceField){
        try {
            String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
            String url = "http://localhost:8080/api/stores/edit-product-price";

            ReqStore request = new ReqStore();
            request.setStoreId(view.getStore_id());
            request.setProductId(view.getProduct_id());
            request.setPrice(newPriceField.getValue());
            request.setUsername(username);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ReqStore> requestEntity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

            ErrorHandler.showSuccessNotification("Successfully updated price");

        } catch (HttpClientErrorException e) {
            ErrorHandler.handleError(e, () -> {
            });
        }
    }

    private void onUpdateInventory(IntegerField newInventoryField){
        try {
            String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
            String url = "http://localhost:8080/api/stores/edit-product-quantity";

            ReqStore request = new ReqStore();
            request.setStoreId(view.getStore_id());
            request.setProductId(view.getProduct_id());
            request.setQuantity(newInventoryField.getValue());
            request.setUsername(username);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ReqStore> requestEntity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

            ErrorHandler.showSuccessNotification("Successfully updated inventory");

        } catch (HttpClientErrorException e) {
            ErrorHandler.handleError(e, () -> {
            });
        }
    }

    private void onAddToCart() {
        try {
            String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
            if(username == null){
                username =(String) VaadinSession.getCurrent().getAttribute("guest-user");
                if(username == null)
                    return;
            }
            String url = "http://localhost:8080/api/users/add-to-cart";

            cartOp request = new cartOp();
            request.setStoreId(view.getStore_id());
            request.setProductId(view.getProduct_id());
            request.setQuantity(view.getQuantity_field().getValue());
            request.setUsername(username);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<cartOp> requestEntity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            ErrorHandler.showSuccessNotification("Successfully added product to cart");

        } catch (HttpClientErrorException e) {
            ErrorHandler.handleError(e, () -> {
            });
        }
    }
}

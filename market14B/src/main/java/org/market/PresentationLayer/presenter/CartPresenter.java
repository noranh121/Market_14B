package org.market.PresentationLayer.presenter;

import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.handlers.ErrorHandler;
import org.market.PresentationLayer.views.CartView;
import org.market.Web.DTOS.CartItemDTO;
import org.market.Web.Requests.cartOp;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class CartPresenter {

    private CartView view;
    private RestTemplate restTemplate;


    public CartPresenter(CartView cartView){
        this.view = cartView;
        this.restTemplate = new RestTemplate();
        initView();
    }

    private void initView() {
        ArrayList<CartItemDTO> products = getCart();
        this.view.loadCart(products);
    }

    private ArrayList<CartItemDTO> getCart() {
        ArrayList<CartItemDTO> result = new ArrayList<>();
        try{
            String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
            if(username == null){
                username = (String) VaadinSession.getCurrent().getAttribute("guest-user");
                if(username == null)
                    return new ArrayList<>();
            }
            String cartUrl = "http://localhost:8080/api/users/cart/{username}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<ArrayList<CartItemDTO>> response = restTemplate.exchange(cartUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<ArrayList<CartItemDTO>>() {}, username);

            result = response.getBody();

        }catch (HttpClientErrorException e){
            ErrorHandler.handleError(e, ()->{});
        }
        return result;
    }

    public void updateCartItemQuantity(CartItemDTO item) {
        if (item != null) {
            try {
                String url = "http://localhost:8080/api/users/add-to-cart";

                cartOp request = new cartOp();
                request.setStoreId(item.getStoreId());
                request.setProductId(item.getProductId());
                request.setQuantity(item.getQuantity());
                request.setUsername(item.getUsername());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<cartOp> requestEntity = new HttpEntity<>(request, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                ErrorHandler.showSuccessNotification("Successfully updated cart item");

            } catch (HttpClientErrorException e) {
                ErrorHandler.handleError(e, () -> {
                });
            }
        }
    }

    public void removeCartItem(CartItemDTO item) {
        if (item != null) {
            try {
                String url = "http://localhost:8080/api/users/remove-cart-item";

                cartOp request = new cartOp();
                request.setStoreId(item.getStoreId());
                request.setProductId(item.getProductId());
                request.setUsername(item.getUsername());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<cartOp> requestEntity = new HttpEntity<>(request, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                ErrorHandler.showSuccessNotification("Successfully removed cart item");

            } catch (HttpClientErrorException e) {
                ErrorHandler.handleError(e, () -> {
                });
            }
        }
    }
}

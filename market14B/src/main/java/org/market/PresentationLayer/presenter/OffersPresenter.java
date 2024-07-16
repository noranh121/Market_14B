package org.market.PresentationLayer.presenter;

import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.handlers.ErrorHandler;
import org.market.PresentationLayer.views.OffersView;
import org.market.Web.DTOS.OfferDTO;
import org.market.Web.Requests.OfferReq;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class OffersPresenter {

    private OffersView view;
    private RestTemplate restTemplate;
    public OffersPresenter(OffersView offersView){
        this.view = offersView;
        this.restTemplate = new RestTemplate();
        initView();
    }

    private void initView() {
        List<OfferDTO> offers = getStoreOffers(view.getStore_id());
        view.createViewOffersLayout(offers);
    }

    private List<OfferDTO> getStoreOffers(int store_id) {
        List<OfferDTO> offers = new ArrayList<>();
        try {
            String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
            String url = "http://localhost:8080/api/stores/offers/user={username}&store={store_id}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<List<OfferDTO>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<OfferDTO>>() {}, username, store_id);

            offers = response.getBody();

            ErrorHandler.showSuccessNotification("Successfully retrieved store offers");

        } catch (HttpClientErrorException e) {
            ErrorHandler.handleError(e, () -> {
            });
        }
        return offers;
    }

    public void approveOffer(Integer storeId, int productId, String offerUsername, double offerPrice) {
        try {
            String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
            String url = "http://localhost:8080/api/stores/approve-offer";

            OfferReq request = new OfferReq();
            request.setOfferUsername(offerUsername);
            request.setUsername(username);
            request.setProductId(productId);
            request.setStoreId(storeId);
            request.setPrice(offerPrice);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<OfferReq> requestEntity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            ErrorHandler.showSuccessNotification("Successfully approved the offer");

        } catch (HttpClientErrorException e) {
            ErrorHandler.handleError(e, () -> {
            });
        }
    }

    public void disapproveOffer(Integer storeId, int productId, String offerUsername) {
        try {
            String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
            String url = "http://localhost:8080/api/stores/reject-offer";

            OfferReq request = new OfferReq();
            request.setOfferUsername(offerUsername);
            request.setUsername(username);
            request.setProductId(productId);
            request.setStoreId(storeId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<OfferReq> requestEntity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            ErrorHandler.showSuccessNotification("Successfully rejected the offer");


        } catch (HttpClientErrorException e) {
            ErrorHandler.handleError(e, () -> {
            });
        }
    }
}

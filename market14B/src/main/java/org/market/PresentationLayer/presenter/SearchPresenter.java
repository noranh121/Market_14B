package org.market.PresentationLayer.presenter;

import org.market.PresentationLayer.handlers.ErrorHandler;
import org.market.PresentationLayer.views.components.SearchBar;
import org.market.Web.DTOS.ProductDTO;
import org.market.Web.Requests.SearchEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter {

    private SearchBar view;
    private RestTemplate restTemplate;

    public SearchPresenter(SearchBar searchBar){
        this.view = searchBar;
        this.restTemplate = new RestTemplate();
        initView();

    }

    private void initView() {
        view.setSearchKeyPressEventListener(e -> {
            if(view.getForProducts()) {
                view.loadResults(onSearchProducts());
            }
        });
    }

    private List<ProductDTO> onSearchProducts() {
        List<ProductDTO> result = new ArrayList<>();
        try {
                String productSearchUrl = "http://localhost:8080/api/stores/search-products";

                SearchEntity request = new SearchEntity();
                request.setCategory(view.getCategoryCheckbox().getValue());
                request.setProductName(view.getSearchField().getValue());
                request.setCategoryName(view.getSearchField().getValue());
                request.setKeyword(view.getSearchField().getValue());
                request.setSearchBy(view.getCategoryComboBox().getValue().equals("Product Name") ? "prod" : view.getCategoryComboBox().getValue().equals("Category Name") ? "cat" : "key");
                request.setInStore(view.getInStore());
                request.setStoreID(view.getStore_id());
                request.setMax(view.getMaxPriceField().getValue());
                request.setMin(view.getMinPriceField().getValue());
                request.setProdRating(view.getProductRatingCheckBox().getValue());
                request.setRange(true);
                request.setStoreRating(view.getStoreRatingCheckBox().getValue());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<SearchEntity> requestEntity = new HttpEntity<>(request, headers);

                ResponseEntity<List<ProductDTO>> response = restTemplate.exchange(productSearchUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<ProductDTO>>() {});

                ErrorHandler.showSuccessNotification("Successfully retrieved search result");

                result = response.getBody();

        } catch(HttpClientErrorException e){
            ErrorHandler.handleError(e, ()->{});
        }
        return result;
    }
}

package org.market.PresentationLayer.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import org.market.PresentationLayer.views.components.ProductCollection;
import org.market.PresentationLayer.views.components.SearchBar;
import org.market.Web.DTOS.ProductDTO;

import java.util.List;

@Route(value = "market", layout =  HomeView.class)
@RoutePrefix("dash")
public class MarketView extends VerticalLayout {

    private ProductCollection collection;
    public MarketView() {
        addClassName("scrollable-layout");

        this.collection = new ProductCollection(-1);

        SearchBar searchBar = new SearchBar(this, null, true, false, false, -1);
        searchBar.addClassName("top-bar");

        add(searchBar);
        add(collection);
    }

    public void loadResults(List<ProductDTO> productDTOS) {
        collection.loadProducts(productDTOS);
    }
}
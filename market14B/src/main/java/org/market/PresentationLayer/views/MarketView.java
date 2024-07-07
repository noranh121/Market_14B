package org.market.PresentationLayer.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.market.PresentationLayer.views.components.ProductCollection;
import org.market.PresentationLayer.views.components.SearchBar;

@Route(value = "market", layout =  HomeView.class)
@RoutePrefix("dash")
@AnonymousAllowed
public class MarketView extends VerticalLayout {
    public MarketView() {
        addClassName("scrollable-layout");

        SearchBar searchBar = new SearchBar(false);
        searchBar.addClassName("top-bar");

        add(searchBar);
        ProductCollection products = new ProductCollection(null);
        add(products);
    }

}
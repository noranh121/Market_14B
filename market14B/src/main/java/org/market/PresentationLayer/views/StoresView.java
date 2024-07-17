package org.market.PresentationLayer.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.market.PresentationLayer.views.components.SearchBar;
import org.market.PresentationLayer.views.components.StoreCollection;

@Route(value = "stores", layout =  HomeView.class)
@RoutePrefix("dash")
@AnonymousAllowed
public class StoresView extends VerticalLayout {

    public StoresView() {
        addClassName("scrollable-layout");

        HorizontalLayout stores_bar = new HorizontalLayout();
        H2 product_title = new H2("Stores");

        HorizontalLayout filler = new HorizontalLayout();
        filler.getStyle().set("width", "100%");

        SearchBar searchBar = new SearchBar( null,null, false, false, true, -1);

        stores_bar.add(product_title);
        stores_bar.addAndExpand(filler);
        stores_bar.add(searchBar);

        stores_bar.addClassName("top-bar");
        add(stores_bar);

        StoreCollection stores = new StoreCollection();

        add(stores);
    }
}

package org.market.PresentationLayer.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import org.market.PresentationLayer.views.components.ProductCollection;
import org.market.PresentationLayer.views.components.SearchBar;
import org.market.Web.DTOS.StoreDTO;

@Route(value = "store", layout = HomeView.class)
@RoutePrefix("dash")
public class StoreView extends VerticalLayout implements HasUrlParameter<String> {

    private StoreDTO store;

    public StoreView(){
        addClassName("store-view");
        addClassName("scrollable-layout");

        HorizontalLayout store_layout = getStoreLayout();

        add(store_layout);

        HorizontalLayout middle_layout = new HorizontalLayout();
        H2 product_title = new H2("Products");

        HorizontalLayout filler = new HorizontalLayout();
        filler.getStyle().set("width", "100%");

        SearchBar searchBar = new SearchBar(true);

        middle_layout.add(product_title);
        middle_layout.addAndExpand(filler);
        middle_layout.add(searchBar);

        middle_layout.addClassName("middle-bar");
        add(middle_layout);

        ProductCollection products = new ProductCollection("store_id");

        add(products);
    }

    private static HorizontalLayout getStoreLayout() {
        HorizontalLayout store_layout = new HorizontalLayout();

        VerticalLayout info_layout = new VerticalLayout();

        Image img = new Image("icons/store.jpeg", "store_image");
        img.addClassName("store-image");
        store_layout.add(img);

        Span storeNameSpan = new Span("Electronic Store");
        storeNameSpan.addClassName("store-name");

        Span addressSpan = new Span("Address: " + "Far away");
        addressSpan.addClassName("store-address");

        Span contactInfoSpan = new Span("Contact: " + "+123456789");
        contactInfoSpan.addClassName("store-contact-info");

        info_layout.add(storeNameSpan, addressSpan, contactInfoSpan);

        store_layout.add(info_layout);
        return store_layout;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        this.store = new StoreDTO();
        try {
            if (s != null && !s.isEmpty()) {
                this.store.setId(Integer.parseInt(s));
                System.out.println("Store ID set to: " + this.store.getId());
            } else {
                System.out.println("Invalid or empty parameter provided.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + e.getMessage());
        }
    }
}

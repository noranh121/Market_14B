package org.market.PresentationLayer.views.components;

import org.market.PresentationLayer.models.Store;
import org.market.PresentationLayer.views.StoreView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MyStoreCard extends VerticalLayout {
    public MyStoreCard(Store store){
        addClassName("store-card");
        Span name = new Span(store.getName());
        name.addClassName("store-name");

        Span description = new Span(store.getDescription());
        description.addClassName("store-description");

        add(name, description);
        addClickListener(e -> this.getUI().ifPresent(ui -> ui.navigate(StoreView.class)));
    }
}

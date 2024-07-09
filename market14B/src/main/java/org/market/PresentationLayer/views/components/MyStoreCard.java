package org.market.PresentationLayer.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.market.PresentationLayer.models.Store;
import org.market.PresentationLayer.views.StoreView;

public class MyStoreCard extends HorizontalLayout {
    public MyStoreCard(Store store){
        addClassName("store-card");

        VerticalLayout layout = new VerticalLayout();
        Span name = new Span(store.getName());
        name.addClassName("store-name");

        Span description = new Span(store.getDescription());
        description.addClassName("store-description");

        layout.add(name, description);
        add(layout);
        addAndExpand(new HorizontalLayout());

        Icon edit = new Icon(VaadinIcon.EDIT);
        edit.addClickListener(e-> UI.getCurrent().navigate("dash/settings"));
        add(edit);

        layout.addClickListener(e -> this.getUI().ifPresent(ui -> ui.navigate(StoreView.class)));
    }
}

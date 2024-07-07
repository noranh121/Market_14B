package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.market.Web.DTOS.StoreDTO;

public class StoreCard extends VerticalLayout{

    public StoreCard(StoreDTO store){
        addClassName("collection-card");

        Image storeImage = new Image("icons/store.jpeg", store.getName());
        storeImage.addClassName("image");

        Span storeTitle = new Span(store.getName());
        storeTitle.addClassName("title");

        HorizontalLayout info = new HorizontalLayout();

        info.add(storeTitle);
        info.addClassName("store-collection-card-info");

        add(storeImage, info);
        addClickListener(e-> UI.getCurrent().navigate("/dash/store/" + store.getId()));
    }
}

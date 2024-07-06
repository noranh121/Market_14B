package org.market.PresentationLayer.views.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class DashImage extends VerticalLayout {

    public DashImage(){
        Image img = new Image("/icons/dash.png", "dash");
        img.setWidth("300px");
        img.setHeight("300px");

        HorizontalLayout layout = new HorizontalLayout(img);
        layout.setWidth("1120px");
        layout.getStyle().set("display", "flex");
        layout.getStyle().set("justify-content", "center");
        layout.getStyle().set("align-items", "center");

        // Center the image within this layout
        setSizeFull(); // Set the size of the layout to 100% width and height
        setAlignItems(Alignment.CENTER); // Center items horizontally
        getStyle().set("display", "flex");
        getStyle().set("justify-content", "center");
        getStyle().set("align-items", "center");

        add(layout);
    }
}

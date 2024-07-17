package org.market.PresentationLayer.views.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.market.PresentationLayer.handlers.PermissionHandler;
import org.market.Web.DTOS.ProductDTO;

public class ProductCard extends VerticalLayout {
    private boolean buttonClicked = false;
    private Button close_btn;

    public ProductCard(ProductDTO product, boolean editable) {
        addClassName("collection-card");

        // Create the product image
        Image productImage = new Image("icons/headphones.jpg", product.getName());
        productImage.addClassName("image");

        // Create the product title
        Span productTitle = new Span(product.getName());
        productTitle.addClassName("title");

        // Create the product price
        Span productPrice = new Span("$" + product.getPrice());
        productPrice.addClassName("price");

        // Create the info layout
        HorizontalLayout info = new HorizontalLayout();
        HorizontalLayout filler = new HorizontalLayout();

        info.add(productTitle);
        info.addAndExpand(filler);
        info.add(productPrice);
        info.addClassName("info");

        // Create a layout to hold the close button and add it to the card
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthFull();

        this.close_btn = new Button(VaadinIcon.CLOSE.create());
        this.close_btn.addClassName("collection-card-remove-button");
        this.close_btn.addClickListener(e -> {
            removeCard();
            buttonClicked = true;
        });

        if(editable && PermissionHandler.hasPermission(product.getStoreid(), 0)) {
            topLayout.add(close_btn);
        }

        topLayout.setAlignItems(Alignment.END);

        add(topLayout, productImage, info);
        addClickListener(e -> {
            if (!buttonClicked) {
                UI.getCurrent().navigate("/dash/product/" + product.getId());
            }
            buttonClicked = false; // Reset the flag
        });
    }

    public void setRemoveProductClickEventListener(ComponentEventListener<ClickEvent<Button>> e){
        this.close_btn.addClickListener(e);
    }

    private void removeCard() {
        // Logic to remove or hide the card
        this.removeFromParent();
    }
}

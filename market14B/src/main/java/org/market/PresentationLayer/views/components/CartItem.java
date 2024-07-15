package org.market.PresentationLayer.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import org.market.Web.DTOS.CartItemDTO;

import java.util.function.Consumer;

public class CartItem extends HorizontalLayout {
    private CartItemDTO item;
    private Span name;
    private Span price;
    private IntegerField quantity;
    private Button deleteButton;

    public CartItem(CartItemDTO item, Consumer<CartItemDTO> onUpdate,  Consumer<CartItemDTO> onDelete) {
        this.item = item;

        // Image
        Image img = new Image("icons/headphones.jpg", item.getName());
        img.addClassName("cart-item-image");
        img.setHeight("100px");
        img.setWidth("100px");
        add(img);

        // Product info layout
        VerticalLayout infoLayout = new VerticalLayout();
        infoLayout.addClassName("cart-item-info-layout");

        name = new Span(item.getName());
        name.addClassName("cart-item-name");
        infoLayout.add(name);

        HorizontalLayout priceQuantityLayout = new HorizontalLayout();
        priceQuantityLayout.addClassName("cart-item-price-quantity-layout");

        price = new Span("$" + item.getPrice());
        price.addClassName("cart-item-price");

        quantity = new IntegerField();
        quantity.setValue(item.getQuantity());
        quantity.setMin(1);
        quantity.addValueChangeListener(e -> {
            item.setQuantity(this.quantity.getValue());
            onUpdate.accept(item);
        });

        priceQuantityLayout.add(price, quantity);
        infoLayout.add(priceQuantityLayout);

        add(infoLayout);

        // Total price and delete button
        HorizontalLayout rightLayout = new HorizontalLayout();
        rightLayout.addClassName("cart-item-right-layout");

        Icon trash = new Icon(VaadinIcon.TRASH);
        deleteButton = new Button(trash);
        deleteButton.addClassName("delete-button");
        deleteButton.addClickListener(e -> {
                    onDelete.accept(item);
                    removeFromParent();
        });

        rightLayout.add(deleteButton);

        add(rightLayout);

        addClassName("cart-item-card");
    }
}

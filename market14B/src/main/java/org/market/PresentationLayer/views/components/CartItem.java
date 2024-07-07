package org.market.PresentationLayer.views.components;

import org.market.PresentationLayer.models.Product;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;

import java.util.function.Consumer;

public class CartItem extends HorizontalLayout {
    private Product product;
    private Span name;
    private Span price;
    private IntegerField quantity;
    private Button deleteButton;
    private Consumer<Product> onUpdate;
    private Runnable onDelete;

    public CartItem(Product product, Consumer<Product> onUpdate, Runnable onDelete) {
        this.product = product;
        this.onUpdate = onUpdate;
        this.onDelete = onDelete;

        // Image
        Image img = new Image("icons/headphones.jpg", product.getName());
        img.addClassName("cart-item-image");
        img.setHeight("100px");
        img.setWidth("100px");
        add(img);

        // Product info layout
        VerticalLayout infoLayout = new VerticalLayout();
        infoLayout.addClassName("cart-item-info-layout");

        name = new Span(product.getName());
        name.addClassName("cart-item-name");
        infoLayout.add(name);

        HorizontalLayout priceQuantityLayout = new HorizontalLayout();
        priceQuantityLayout.addClassName("cart-item-price-quantity-layout");

        price = new Span("$" + product.getPrice());
        price.addClassName("cart-item-price");

        quantity = new IntegerField();
        quantity.setValue(product.getQuantity());
        quantity.setMin(1);
        quantity.addValueChangeListener(e -> {
            product.setQuantity(e.getValue());
            onUpdate.accept(product);
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
        deleteButton.addClickListener(e -> onDelete.run());

        rightLayout.add(deleteButton);

        add(rightLayout);

        addClassName("cart-item-card");
    }
}

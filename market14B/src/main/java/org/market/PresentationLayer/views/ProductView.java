package org.market.PresentationLayer.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.market.PresentationLayer.views.components.ProductForProductView;

@Route(value = "product", layout = HomeView.class)
@RoutePrefix("dash")
@AnonymousAllowed
public class ProductView extends VerticalLayout implements HasUrlParameter<String> {
    private ProductForProductView product;
    private H2 title;
    private Span description;
    private ComboBox<String> sizeSelector;
    private ComboBox<String> colorSelector;
    private IntegerField quantityField;
    private Span price;
    private Button addToCartButton;

    public ProductView() {

        HorizontalLayout top_layout = new HorizontalLayout();

        Image img = new Image("icons/headphones.jpg", "product-view-image");
        img.addClassName("product-image");

        product = new ProductForProductView("product 1", "description",30.0);

        title = new H2(product.getName());
        title.getStyle().set("font-size", "25px");

        description = new Span(product.getDescription());

        sizeSelector = new ComboBox<>();
        sizeSelector.setItems("Small", "Medium", "Large");
        sizeSelector.setLabel("Size");
        sizeSelector.setWidth("7em");
        sizeSelector.getStyle().set("--lumo-size-m", "25px");

        colorSelector = new ComboBox<>();
        colorSelector.setItems("Red", "Blue", "Green");
        colorSelector.setLabel("Color");
        colorSelector.setWidth("7em");
        colorSelector.getStyle().set("--lumo-size-m", "25px");

        quantityField = new IntegerField("Quantity");
        quantityField.setValue(1);
        quantityField.setMin(1);
        quantityField.getStyle().set("--lumo-size-m", "25px");
        quantityField.setWidth("7em");


        price = new Span("$" + product.getPrice());
        price.addClassName("product-view-price");

        addToCartButton = new Button("Add to Cart");
        addToCartButton.addClassName("add-to-cart-btn");
        addToCartButton.setSuffixComponent(VaadinIcon.PLUS_CIRCLE_O.create());

        VerticalLayout specificationsLayout = new VerticalLayout();
        HorizontalLayout specificationBar = new HorizontalLayout(new H2("Specifications"));
        specificationBar.addClassName("specification-bar");

        Div separator1 = new Div();
        Div separator2 = new Div();
        separator1.addClassName("separator");
        separator2.addClassName("separator");

        VerticalLayout specificationsList = new VerticalLayout();
        specificationsList.addClassName("specifications-list");
        specificationsList.add(new Span("Spec 1: Value"));
        specificationsList.add(separator1);
        specificationsList.add(new Span("Spec 2: Value"));
        specificationsList.add(separator2);
        specificationsList.add(new Span("Spec 3: Value"));

        specificationsLayout.add(specificationBar);
        specificationsLayout.add(specificationsList);

        VerticalLayout productLayout = new VerticalLayout();

        productLayout.add(title, description);

        HorizontalLayout details = new HorizontalLayout(sizeSelector,colorSelector);
        HorizontalLayout quantity_price = new HorizontalLayout(quantityField, price);
        VerticalLayout button_layout = new VerticalLayout(addToCartButton);

        button_layout.getStyle().set("width", "auto");
        button_layout.getStyle().set("margin-top", "80px");

        quantity_price.addClassName("product-view-price-quantity");

        productLayout.add(details, quantity_price);

        top_layout.add(img);
        top_layout.add(productLayout);
        top_layout.addAndExpand(new HorizontalLayout());
        top_layout.add(button_layout);
        top_layout.addClassName("product-view-top");

        add(top_layout);
        add(specificationsLayout);
        addClassName("product-view");
    }

    private void addToCart() {
        // Implement add to cart functionality
        System.out.println("Product added to cart!");
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        product.setName(s);
        title.removeAll();
        title.add(s);
    }
}

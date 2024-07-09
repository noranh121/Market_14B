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
import org.market.PresentationLayer.presenter.ProductPresenter;
import org.market.Web.DTOS.ProductDTO;

@Route(value = "product", layout = HomeView.class)
@RoutePrefix("dash")
@AnonymousAllowed
public class ProductView extends VerticalLayout implements HasUrlParameter<String> {

    private int product_id;
    private HorizontalLayout top_layout;
    private ComboBox<String> sizeSelector;
    private ComboBox<String> colorSelector;
    private IntegerField quantityField;
    private Button addToCartButton;

    private ProductPresenter presenter;

    public ProductView() {

        this.top_layout = new HorizontalLayout();

        VerticalLayout specificationsLayout = new VerticalLayout();
        HorizontalLayout specificationBar = new HorizontalLayout(new H2("Specifications"));
        specificationBar.addClassName("specification-bar");

        Div separator1 = new Div();
        Div separator2 = new Div();
        separator1.addClassName("separator");
        separator2.addClassName("separator");

        VerticalLayout specificationsList = new VerticalLayout();
        specificationsList.addClassName("specifications-list");
        specificationsList.add(new Span("Spec 1: not available"));
        specificationsList.add(separator1);
        specificationsList.add(new Span("Spec 2: not available"));
        specificationsList.add(separator2);
        specificationsList.add(new Span("Spec 3: not available"));

        specificationsLayout.add(specificationBar);
        specificationsLayout.add(specificationsList);

        add(top_layout);
        add(specificationsLayout);
        addClassName("product-view");

        this.presenter = new ProductPresenter(this);
    }

    public void setTopLayout(ProductDTO product){
        Image img = new Image("icons/headphones.jpg", "product-view-image");
        img.addClassName("product-image");

        H2 title = new H2(product.getName());
        title.getStyle().set("font-size", "25px");

        Span description = new Span(product.getDescription());

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

        Span price = new Span("$" + product.getPrice());
        price.addClassName("product-view-price");

        addToCartButton = new Button("Add to Cart");
        addToCartButton.addClassName("add-to-cart-btn");
        addToCartButton.setSuffixComponent(VaadinIcon.PLUS_CIRCLE_O.create());

        VerticalLayout productLayout = new VerticalLayout();

        productLayout.add(title, description);

        HorizontalLayout details = new HorizontalLayout(sizeSelector,colorSelector);
        HorizontalLayout quantity_price = new HorizontalLayout(quantityField, price);
        VerticalLayout button_layout = new VerticalLayout(addToCartButton);

        button_layout.getStyle().set("width", "auto");
        button_layout.getStyle().set("margin-top", "80px");

        quantity_price.addClassName("product-view-price-quantity");

        productLayout.add(details, quantity_price);

        this.top_layout.add(img);
        this.top_layout.add(productLayout);
        this.top_layout.addAndExpand(new HorizontalLayout());
        this.top_layout.add(button_layout);
        this.top_layout.addClassName("product-view-top");
    }

    private void addToCart() {
        // Implement add to cart functionality
        System.out.println("Product added to cart!");
    }

    public int getProduct_id() {
        return product_id;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        try {
            if (s != null && !s.isEmpty()) {
                this.product_id = Integer.parseInt(s);
                System.out.println("Product ID set to: " + product_id);
            } else {
                System.out.println("Invalid or empty parameter provided.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + e.getMessage());
        }
    }
}

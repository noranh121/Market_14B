package org.market.PresentationLayer.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.market.PresentationLayer.handlers.PermissionHandler;
import org.market.PresentationLayer.presenter.ProductPresenter;
import org.market.Web.DTOS.ProductDTO;

@Route(value = "product", layout = HomeView.class)
@RoutePrefix("dash")
@AnonymousAllowed
public class ProductView extends VerticalLayout implements HasUrlParameter<String> {

    private int product_id;
    private int store_id;
    private HorizontalLayout top_layout;
    private VerticalLayout specificationsLayout;
    private IntegerField quantityField;
    private Button addToCartButton;
    private HorizontalLayout edit_layout;
    private ProductPresenter presenter;
    private Button open_edit;
    private Button save_edit;
    private NumberField new_price_field;
    private IntegerField new_inventory_field;



    public ProductView() {

        this.top_layout = new HorizontalLayout();

        this.specificationsLayout = new VerticalLayout();

        add(top_layout);
        add(specificationsLayout);
        addClassName("product-view");
    }

    public void setTopLayout(ProductDTO product){
        this.store_id = product.getStoreid();

        Image img = new Image("icons/headphones.jpg", "product-view-image");
        img.addClassName("product-image");

        H2 title = new H2(product.getName());
        title.getStyle().set("font-size", "25px");

        Span description = new Span(product.getDescription());

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

        this.edit_layout = new HorizontalLayout();
        edit_layout.getStyle().set("display", "flex").set("align-items","flex-end");

        this.new_price_field = new NumberField("Edit Price:", "enter new price...");
        this.new_inventory_field = new IntegerField("Edit Inventory:", "enter new inventory...");
        this.new_price_field.setVisible(false);
        this.new_inventory_field.setVisible(false);

        this.open_edit = new Button("Edit");
        this.open_edit.setSuffixComponent(VaadinIcon.EDIT.create());
        this.open_edit.addClassName("manager-setting-btn");
        this.open_edit.addClickListener(e -> {
           setEditLayoutVisible(true);
        });
        this.open_edit.setVisible(PermissionHandler.hasPermission(store_id, 0));

        this.save_edit = new Button("Save");
        this.save_edit.addClassName("manager-setting-btn");
        this.save_edit.setSuffixComponent(VaadinIcon.CHECK.create());
        this.save_edit.setVisible(false);

        edit_layout.add(new_price_field, new_inventory_field, save_edit, open_edit);

        productLayout.add(title, description);


        HorizontalLayout quantity_price = new HorizontalLayout(quantityField, price);
        VerticalLayout button_layout = new VerticalLayout(addToCartButton);

        button_layout.getStyle().set("width", "auto");
        button_layout.getStyle().set("margin-top", "80px");

        quantity_price.addClassName("product-view-price-quantity");

        productLayout.add(quantity_price, edit_layout);

        this.top_layout.add(img);
        this.top_layout.add(productLayout);
        this.top_layout.addAndExpand(new HorizontalLayout());
        this.top_layout.add(button_layout);
        this.top_layout.addClassName("product-view-top");
    }

    public void setSpecificationLayout(ProductDTO product) {
        HorizontalLayout specificationBar = new HorizontalLayout(new H2("Specifications"));
        specificationBar.addClassName("specification-bar");

        Div separator1 = new Div();
        Div separator2 = new Div();
        separator1.addClassName("separator");
        separator2.addClassName("separator");

        HorizontalLayout weight_layout = new HorizontalLayout();
        Span weight_label = new Span("Weight: ");
        Span weight_span = new Span(product.getWeigth() + " kg");
        weight_span.getStyle().set("font-weight", "bold");
        weight_layout.add(weight_label, weight_span);

        HorizontalLayout brand_layout = new HorizontalLayout();
        Span brand_label = new Span("Brand: ");
        Span brand_span = new Span(product.getBrand());
        brand_span.getStyle().set("font-weight", "bold");
        brand_layout.add(brand_label, brand_span);

        HorizontalLayout category_layout = new HorizontalLayout();
        Span category_label = new Span("Category: ");
        Span category_span = new Span(product.getCategory());
        category_span.getStyle().set("font-weight", "bold");
        category_layout.add(category_label, category_span);

        VerticalLayout specificationsList = new VerticalLayout();
        specificationsList.addClassName("specifications-list");
        specificationsList.add(weight_layout);
        specificationsList.add(separator1);
        specificationsList.add(brand_layout);
        specificationsList.add(separator2);
        specificationsList.add(category_layout);

        this.specificationsLayout.add(specificationBar);
        this.specificationsLayout.add(specificationsList);
    }

    public void setSaveButtonClickEventListener(ComponentEventListener<ClickEvent<Button>> e){
        this.save_edit.addClickListener(e);
    }

    public void setAddToCartButtonClickEventListener(ComponentEventListener<ClickEvent<Button>> e){
        this.addToCartButton.addClickListener(e);
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public IntegerField getQuantity_field(){ return quantityField; }

    public NumberField getNew_price_field() {
        return new_price_field;
    }

    public IntegerField getNew_inventory_field() {
        return new_inventory_field;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        try {
            if (s != null && !s.isEmpty()) {
                this.product_id = Integer.parseInt(s);
                this.presenter = new ProductPresenter(this);
                System.out.println("Product ID set to: " + product_id);
            } else {
                System.out.println("Invalid or empty parameter provided.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + e.getMessage());
        }
    }

    public void setEditLayoutVisible(boolean visible) {
        this.save_edit.setVisible(visible);
        this.open_edit.setVisible(!visible);
        this.new_price_field.setVisible(visible);
        this.new_inventory_field.setVisible(visible);
    }
}

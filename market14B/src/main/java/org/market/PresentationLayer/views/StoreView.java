package org.market.PresentationLayer.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import org.market.PresentationLayer.presenter.StorePresenter;
import org.market.PresentationLayer.views.components.ProductCollection;
import org.market.PresentationLayer.views.components.SearchBar;
import org.market.Web.DTOS.StoreDTO;

@Route(value = "store", layout = HomeView.class)
@RoutePrefix("dash")
public class StoreView extends VerticalLayout implements HasUrlParameter<String> {

    private StorePresenter presenter;
    private int store_id;
    private Button add_product_btn;
    private HorizontalLayout store_layout;
    private Dialog add_product_dialog;

    private TextField name_field;
    private TextField description_field;
    private NumberField price_field;
    private NumberField weight_field;
    private IntegerField inventory_field;
    private Button apply_btn;

    private Span storeNameSpan;
    public StoreView(){
        addClassName("store-view");
        addClassName("scrollable-layout");

        this.store_layout = new HorizontalLayout();

        add(store_layout);

        HorizontalLayout middle_layout = new HorizontalLayout();
        H2 product_title = new H2("Products");

        HorizontalLayout filler = new HorizontalLayout();
        filler.getStyle().set("width", "100%");

        SearchBar searchBar = new SearchBar(true);



        middle_layout.add(product_title);
        middle_layout.addAndExpand(filler);
        middle_layout.add(searchBar);

        if(true) { // TODO - ADD PERMISSION CHECK
            this.add_product_btn = new Button("Add Product");
            add_product_btn.setSuffixComponent(VaadinIcon.PLUS_CIRCLE_O.create());
            add_product_btn.addClassName("add-product-btn");
            middle_layout.add(add_product_btn);

            VerticalLayout dialog_layout = new VerticalLayout();

            this.name_field = new TextField("Name");
            this.name_field.setRequired(true);
            this.name_field.setRequiredIndicatorVisible(true);

            this.description_field = new TextField("Description");
            this.description_field.setRequired(true);
            this.description_field.setRequiredIndicatorVisible(true);

            this.price_field = new NumberField("Price");
            this.price_field.setRequired(true);
            this.price_field.setRequiredIndicatorVisible(true);
            price_field.setValue(1.0);
            price_field.setMin(1.0);

            this.weight_field = new NumberField("Weight");
            this.weight_field.setRequired(true);
            this.weight_field.setRequiredIndicatorVisible(true);
            weight_field.setValue(0.0);
            weight_field.setMin(0.0);

            this.inventory_field = new IntegerField("Inventory");
            this.inventory_field.setRequired(true);
            this.inventory_field.setRequiredIndicatorVisible(true);
            inventory_field.setValue(1);
            inventory_field.setMin(1);

            HorizontalLayout apply_layout = new HorizontalLayout();
            this.apply_btn = new Button("Add");
            apply_layout.add(apply_btn);
            apply_btn.addClassName("store-apply-btn");
            apply_layout.addClassName("store-apply-layout");

            dialog_layout.add(name_field, description_field, price_field, weight_field, inventory_field, apply_layout);
            this.add_product_dialog = new Dialog(dialog_layout);
            this.add_product_btn.addClickListener(e -> add_product_dialog.open());
        }


        middle_layout.addClassName("middle-bar");
        add(middle_layout);
    }

    public void setApplyButtonClickEventListener(ComponentEventListener<ClickEvent<Button>> event) {
        this.apply_btn.addClickListener(event);
    }

    private boolean validateAddForm() {
        boolean isValid = true;

        if (name_field.isEmpty()) {
            name_field.setInvalid(true);
            name_field.setErrorMessage("Name is required");
            isValid = false;
        }
        if (description_field.isEmpty()) {
            description_field.setInvalid(true);
            description_field.setErrorMessage("Description is required");
            isValid = false;
        }
        if (price_field.isEmpty()) {
            price_field.setInvalid(true);
            price_field.setErrorMessage("Price is required");
            isValid = false;
        }

        if (weight_field.isEmpty()) {
            weight_field.setInvalid(true);
            weight_field.setErrorMessage("Weight is required");
            isValid = false;
        }
        if (inventory_field.isEmpty()) {
            inventory_field.setInvalid(true);
            inventory_field.setErrorMessage("Inventory is required");
            isValid = false;
        }

        return isValid;
    }

    public void setStoreLayout(StoreDTO store) {
        VerticalLayout info_layout = new VerticalLayout();

        Image img = new Image("icons/store.jpeg", "store_image");
        img.addClassName("store-image");
        this.store_layout.add(img);

        Span storeNameSpan = new Span(store.getName());
        storeNameSpan.addClassName("store-name");

        Span description = new Span(store.getDescription());
        description.addClassName("store-description");

        info_layout.add(storeNameSpan, description);

        this.store_layout.add(info_layout);
    }

    public int getStore_id() {
        return store_id;
    }

    public TextField getName_field() {
        return name_field;
    }

    public TextField getDescription_field() {
        return description_field;
    }

    public NumberField getPrice_field() {
        return price_field;
    }

    public NumberField getWeight_field() {
        return weight_field;
    }

    public IntegerField getInventory_field() {
        return inventory_field;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        try {
            if (s != null && !s.isEmpty()) {
                this.store_id = Integer.parseInt(s);
                System.out.println("Store ID set to: " + store_id);
                this.presenter = new StorePresenter(this);
                ProductCollection products = new ProductCollection(store_id);
                add(products);
            } else {
                System.out.println("Invalid or empty parameter provided.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + e.getMessage());
        }
    }
}

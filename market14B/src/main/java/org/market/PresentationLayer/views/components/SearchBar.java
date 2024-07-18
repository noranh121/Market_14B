package org.market.PresentationLayer.views.components;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyPressEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import org.market.PresentationLayer.presenter.SearchPresenter;
import org.market.PresentationLayer.views.MarketView;
import org.market.PresentationLayer.views.StoreView;
import org.market.Web.DTOS.ProductDTO;

import java.util.List;

public class SearchBar extends VerticalLayout {

    private Boolean forProducts;
    private Boolean inStore;
    private int store_id;
    private Icon settingsIcon;
    private Icon filterIcon;
    private TextField searchField;
    private ComboBox<Object> categoryComboBox;
    private IntegerField minPriceField;
    private IntegerField maxPriceField;
    private Checkbox productRatingCheckBox;
    private Checkbox storeRatingCheckBox;
    private Checkbox categoryCheckbox;
    private SearchPresenter presenter;
    private MarketView market;
    private StoreView store;

    public SearchBar(MarketView market, StoreView store,boolean products, boolean inStore, boolean variant, int store_id){
        this.market = market;
        this.store = store;
        this.forProducts = products;
        this.inStore = inStore;
        this.store_id = store_id;
        searchField = new TextField();
        searchField.setPlaceholder("Search...");

        searchField.addClassName(!variant ? "search-field" : "search-field-variant");

        Icon searchIcon = new Icon(VaadinIcon.SEARCH);

        searchField.setPrefixComponent(searchIcon);

        settingsIcon = new Icon(VaadinIcon.COG);
        settingsIcon.addClassName(!variant ? "settings-icon" : "settings-icon-variant");
        settingsIcon.addClickListener(e -> openSearchDialog());


        filterIcon = new Icon(VaadinIcon.SLIDERS);
        filterIcon.addClassName("filter-icon");
        filterIcon.addClickListener(e -> openFilterDialog());

        HorizontalLayout searchLayout = new HorizontalLayout(searchField, settingsIcon, filterIcon);
        searchLayout.addClassName("search-layout");
        searchLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        add(searchLayout);

        this.presenter = new SearchPresenter(this);
    }

    private void openSearchDialog() {
        Dialog searchdialog = new Dialog();
        searchdialog.setWidth("300px");

        searchdialog.getElement().getStyle().set("position", "absolute");

        VerticalLayout layout = new VerticalLayout();

        this.categoryComboBox = new ComboBox<>("Search by");
        categoryComboBox.setItems("Product Name", "Category Name", "Keyword");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button applyButton = new Button("Apply", event -> {
        });
        Button resetButton = new Button("Reset", event -> {
            // Reset search settings logic here
            categoryComboBox.clear();
        });

        buttonLayout.add(applyButton, resetButton);

        layout.add(categoryComboBox, buttonLayout);
        searchdialog.add(layout);
        searchdialog.open();
    }

    private void openFilterDialog(){
        Dialog filterdialog = new Dialog();
        filterdialog.setWidth("300px");

        filterdialog.getElement().getStyle().set("position", "absolute");

        VerticalLayout layout = new VerticalLayout();

        this.minPriceField = new IntegerField("Min Price");
        this.maxPriceField = new IntegerField("Max Price");

        this.productRatingCheckBox = new Checkbox("Product Rating");
        this.storeRatingCheckBox = new Checkbox("Store Rating");
        this.categoryCheckbox = new Checkbox("Category");



        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button applyButton = new Button("Apply", event -> {
        });
        Button resetButton = new Button("Reset", event -> {
            // Reset filter logic here
            productRatingCheckBox.clear();
            minPriceField.clear();
            maxPriceField.clear();
            storeRatingCheckBox.clear();
            categoryCheckbox.clear();
        });

        buttonLayout.add(applyButton, resetButton);

        layout.add(minPriceField, maxPriceField, productRatingCheckBox, storeRatingCheckBox, categoryCheckbox, buttonLayout);
        filterdialog.add(layout);
        filterdialog.open();
    }

    public void setSearchKeyPressEventListener(ComponentEventListener<KeyPressEvent> e){
        searchField.addKeyPressListener(Key.ENTER, e);
    }

    public Boolean getForProducts() {
        return forProducts;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public ComboBox<Object> getCategoryComboBox() {
        return categoryComboBox;
    }

    public IntegerField getMinPriceField() {
        return minPriceField;
    }

    public IntegerField getMaxPriceField() {
        return maxPriceField;
    }

    public Checkbox getProductRatingCheckBox() {
        return productRatingCheckBox;
    }

    public Checkbox getStoreRatingCheckBox() {
        return storeRatingCheckBox;
    }

    public Checkbox getCategoryCheckbox() {
        return categoryCheckbox;
    }

    public Boolean getInStore() {
        return inStore;
    }

    public int getStore_id() {
        return store_id;
    }

        public void loadResults(List<ProductDTO> productDTOS) {
        if(inStore){
            store.loadResults(productDTOS);
        }
        else{
            market.loadResults(productDTOS);
        }
    }
}

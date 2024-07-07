package org.market.PresentationLayer.views.components;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class SearchBar extends VerticalLayout {

    private TextField searchField;
    private Icon settingsIcon;
    private Icon filterIcon;
    public SearchBar(boolean variant){
        searchField = new TextField();
        searchField.setPlaceholder("Search...");

        searchField.addKeyPressListener(Key.ENTER, event -> {
            // Handle the Enter key press event
            String value = searchField.getValue();
            System.out.println("Entered text: " + value);
        });


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
    }

    private void openSearchDialog() {
        Dialog filterdialog = new Dialog();
        filterdialog.setWidth("300px");

        filterdialog.getElement().getStyle().set("position", "absolute");

        VerticalLayout layout = new VerticalLayout();

        ComboBox<String> categoryComboBox = new ComboBox<>("Category");
        categoryComboBox.setItems("Clothing", "Electronics", "Books", "Furniture");

        TextField minPriceField = new TextField("Min Price");
        TextField maxPriceField = new TextField("Max Price");

        TextField storeField = new TextField("Store");

        ComboBox<String> ratingComboBox = new ComboBox<>("Rating");
        ratingComboBox.setItems("1 Star", "2 Stars", "3 Stars", "4 Stars", "5 Stars");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button applyButton = new Button("Apply", event -> {
            filterdialog.close();
        });
        Button resetButton = new Button("Reset", event -> {
            // Reset filter logic here
            categoryComboBox.clear();
            minPriceField.clear();
            maxPriceField.clear();
            storeField.clear();
            ratingComboBox.clear();
        });

        buttonLayout.add(applyButton, resetButton);

        layout.add(categoryComboBox, minPriceField, maxPriceField, storeField, ratingComboBox, buttonLayout);
        filterdialog.add(layout);
        filterdialog.open();
    }

    private void openFilterDialog(){
        Dialog filterdialog = new Dialog();
        filterdialog.setWidth("300px");

        filterdialog.getElement().getStyle().set("position", "absolute");

        VerticalLayout layout = new VerticalLayout();

        ComboBox<String> categoryComboBox = new ComboBox<>("Category");
        categoryComboBox.setItems("Clothing", "Electronics", "Books", "Furniture");

        TextField minPriceField = new TextField("Min Price");
        TextField maxPriceField = new TextField("Max Price");

        TextField storeField = new TextField("Store");

        ComboBox<String> ratingComboBox = new ComboBox<>("Rating");
        ratingComboBox.setItems("1 Star", "2 Stars", "3 Stars", "4 Stars", "5 Stars");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button applyButton = new Button("Apply", event -> {
            filterdialog.close();
        });
        Button resetButton = new Button("Reset", event -> {
            // Reset filter logic here
            categoryComboBox.clear();
            minPriceField.clear();
            maxPriceField.clear();
            storeField.clear();
            ratingComboBox.clear();
        });

        buttonLayout.add(applyButton, resetButton);

        layout.add(categoryComboBox, minPriceField, maxPriceField, storeField, ratingComboBox, buttonLayout);
        filterdialog.add(layout);
        filterdialog.open();
    }
}

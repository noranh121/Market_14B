package org.market.PresentationLayer.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import org.market.PresentationLayer.presenter.PaymentPresenter;

@Route(value = "payment", layout = HomeView.class)
@RoutePrefix("dash/cart")
public class PaymentView extends VerticalLayout {

    private PaymentPresenter presenter;
    private Button submitButton;
    private TextField username;
    private ComboBox<String> currency;
    private TextField cardNumber;
    private ComboBox<Integer> month;
    private ComboBox<Integer> year;
    private TextField ccv;
    private TextField address;
    private TextField city;
    private ComboBox<String> country;
    private IntegerField zip;

    public PaymentView() {
        // Form Layout
        FormLayout formLayout = new FormLayout();
        formLayout.addClassName("payment-form");

        // Username
        username = new TextField("Username");
        username.setPrefixComponent(VaadinIcon.USER.create());

        // Currency
        currency = new ComboBox<>("Currency");
        currency.setItems("USD", "EUR", "GBP");
        currency.setPrefixComponent(VaadinIcon.MONEY.create());

        // Card Number
        cardNumber = new TextField("Card Number");
        cardNumber.setPrefixComponent(VaadinIcon.CREDIT_CARD.create());

        // Month
        month = new ComboBox<>("Month");
        month.setItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        // Year
        year = new ComboBox<>("Year");
        year.setItems(2024, 2025, 2026, 2027, 2028);

        // CCV
        ccv = new TextField("CCV");
        ccv.setPrefixComponent(VaadinIcon.KEY.create());

        // Address
        address = new TextField("Address");
        address.setPrefixComponent(VaadinIcon.HOME.create());

        // City
        city = new TextField("City");
        city.setPrefixComponent(VaadinIcon.BUILDING.create());

        // Country
        country = new ComboBox<>("Country");
        country.setItems("USA", "Canada", "UK", "Germany", "France");

        // ZIP
        zip = new IntegerField("ZIP Code");
        zip.setPrefixComponent(VaadinIcon.MAILBOX.create());

        // Add components to the form
        formLayout.add(username, currency, cardNumber, month, year, ccv, address, city, country, zip);

        // Submit button
        this.submitButton = new Button("Submit", VaadinIcon.CHECK.create());
        submitButton.addClickListener(e -> {
            // Handle form submission
        });

        // Add form and button to layout
        add(formLayout, submitButton);

        this.presenter = new PaymentPresenter(this);
    }

    public void setSubmitClickEventListener(ComponentEventListener<ClickEvent<Button>> e){
        this.submitButton.addClickListener(e);
    }

    public TextField getUsername() {
        return username;
    }

    public ComboBox<String> getCurrency() {
        return currency;
    }

    public TextField getCardNumber() {
        return cardNumber;
    }

    public ComboBox<Integer> getMonth() {
        return month;
    }

    public ComboBox<Integer> getYear() {
        return year;
    }

    public TextField getCcv() {
        return ccv;
    }

    public TextField getAddress() {
        return address;
    }

    public TextField getCity() {
        return city;
    }

    public ComboBox<String> getCountry() {
        return country;
    }

    public IntegerField getZip() {
        return zip;
    }
}

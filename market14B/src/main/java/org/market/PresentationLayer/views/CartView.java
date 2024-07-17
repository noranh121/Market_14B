package org.market.PresentationLayer.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import org.market.PresentationLayer.presenter.CartPresenter;
import org.market.PresentationLayer.views.components.CartItem;
import org.market.Web.DTOS.CartItemDTO;

import java.util.ArrayList;
import java.util.List;

@Route(value = "cart", layout = HomeView.class)
@RoutePrefix("dash")
public class CartView extends VerticalLayout implements BeforeEnterObserver {
    private Span subtitle;
    private List<CartItemDTO> items = new ArrayList<>();
    private VerticalLayout productsLayout;
    private Span totalPrice;
    private Button checkoutButton;
    private CartPresenter presenter;

    public CartView() {

        Span title = new Span("Your cart");
        title.addClassName("cart-title");

        this.subtitle = new Span(items.size() + " items at checkout");

        subtitle.addClassName("cart-subtitle");

        add(title, subtitle);

        productsLayout = new VerticalLayout();
        productsLayout.addClassName("cart-items-layout");

        totalPrice = new Span();
        updateTotalPriceSpan();
        totalPrice.addClassName("cart-total-price");

        checkoutButton = new Button("Checkout");
        checkoutButton.addClassName("checkout-button");
        checkoutButton.addClickListener(e -> UI.getCurrent().navigate("dash/cart/payment"));

        VerticalLayout summaryLayout = new VerticalLayout();
        summaryLayout.add(new Span("Summary"), totalPrice, checkoutButton);
        summaryLayout.addClassName("summary-layout");



        // Add products and summary to the main layout
        add(productsLayout, summaryLayout);

        addClassName("cart-view");
    }

    private void updateTotalPriceSpan() {
        double totalPrice = calculateTotalPrice();
        this.totalPrice.setText(String.format("Balance: $%.2f", totalPrice));
    }

    private double calculateTotalPrice() {
        return this.items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public void loadCart(ArrayList<CartItemDTO> items) {
        this.items = items;
        checkoutButton.setEnabled(!items.isEmpty());
        this.subtitle.setText(items.size() + " items at checkout");
        updateTotalPriceSpan();
        productsLayout.removeAll();
        for (CartItemDTO item : items) {
            CartItem card = new CartItem(item, e -> {
                presenter.updateCartItemQuantity(e);
                updateTotalPriceSpan();
            }, e -> {
                presenter.removeCartItem(e);
            });
            productsLayout.add(card);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.presenter = new CartPresenter(this);
    }
}

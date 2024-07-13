package org.market.PresentationLayer.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import org.market.PresentationLayer.models.Product;
import org.market.PresentationLayer.views.components.CartItem;

import java.util.ArrayList;
import java.util.List;

@Route(value = "cart", layout = HomeView.class)
@RoutePrefix("dash")
public class CartView extends VerticalLayout {
    private List<Product> products = new ArrayList<>();
    private VerticalLayout productsLayout;
    private Span totalPrice;
    private Button checkoutButton;

    public CartView() {

        products.add(new Product("6 Blade Starter Kit", 10.0, 1));
        products.add(new Product("6 Blade Starter Kit", 10.0, 1));
        products.add(new Product("6 Blade Starter Kit", 10.0, 1));
        products.add(new Product("6 Blade Starter Kit", 10.0, 1));
        products.add(new Product("6 Blade Starter Kit", 10.0, 1));

        Span title = new Span("Your cart");
        title.addClassName("cart-title");

        Span subtitle = new Span(products.size() + " items at checkout");
        subtitle.addClassName("cart-subtitle");

        add(title, subtitle);

        productsLayout = new VerticalLayout();
        productsLayout.addClassName("cart-items-layout");
        updateProducts();

        totalPrice = new Span();
        updateTotalPriceSpan();
        totalPrice.addClassName("cart-total-price");

        checkoutButton = new Button("Checkout");
        checkoutButton.addClassName("checkout-button");

        VerticalLayout summaryLayout = new VerticalLayout();
        summaryLayout.add(new Span("Summary"), totalPrice, checkoutButton);
        summaryLayout.addClassName("summary-layout");



        // Add products and summary to the main layout
        add(productsLayout, summaryLayout);

        addClassName("cart-view");
    }

    private void updateProducts() {
        productsLayout.removeAll();
        for (Product product : products) {
            productsLayout.add(new CartItem(product, this::updateTotalPriceSpan, () -> {
                products.remove(product); // onDelete action
                updateProducts();
                updateTotalPriceSpan();
            }));
        }
    }

    private void updateTotalPriceSpan(Product product) {
        updateTotalPriceSpan();
    }

    private void updateTotalPriceSpan() {
        double totalPrice = calculateTotalPrice();
        this.totalPrice.setText(String.format("Balance: $%.2f", totalPrice));
    }

    private double calculateTotalPrice() {
        return products.stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();
    }
}

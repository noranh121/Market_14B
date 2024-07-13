package org.market.PresentationLayer.views;


import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;


@Route(value = "", layout = MainLayout.class)
@ParentLayout(MainLayout.class)
public class HomeView extends HorizontalLayout implements RouterLayout, AfterNavigationObserver{
    private List<RouterLink> drawerLinks = new ArrayList<>();

    public HomeView() {
        createDrawer();
        setSizeFull();
    }

    private void createDrawer() {
        RouterLink marketLink = new RouterLink("", MarketView.class);
        Icon marketIcon = new Icon(VaadinIcon.SHOP);
        marketIcon.getStyle().set("width", "20px").set("height", "20px").set("margin", "10px");
        marketLink.add(marketIcon);
        marketLink.add("Market");
        marketLink.addClassName("drawer-link");
        marketLink.getStyle().set("margin-top", "10px");

        RouterLink storesLink = new RouterLink("", StoresView.class);
        Icon storesIcon = new Icon(VaadinIcon.BUILDING);
        storesIcon.getStyle().set("width", "20px").set("height", "20px").set("margin", "10px");
        storesLink.add(storesIcon);
        storesLink.add("Stores");
        storesLink.addClassName("drawer-link");

        RouterLink cartLink = new RouterLink("", CartView.class);
        Icon CartIcon = new Icon(VaadinIcon.CART);
        CartIcon.getStyle().set("width", "20px").set("height", "20px").set("margin", "10px");
        cartLink.add(CartIcon);
        cartLink.add("Cart");
        cartLink.addClassName("drawer-link");

        RouterLink mystoresLink = new RouterLink("", MyStoresView.class);
        Icon mystoresIcon = new Icon(VaadinIcon.STORAGE);
        mystoresIcon.getStyle().set("width", "20px").set("height", "20px").set("margin", "10px");
        mystoresLink.add(mystoresIcon);
        mystoresLink.add("My Stores");
        mystoresLink.addClassName("drawer-link");

        RouterLink accountLink = new RouterLink("", Account.class);
        Icon accountIcon = new Icon(VaadinIcon.USER);
        accountIcon.getStyle().set("width", "20px").set("height", "20px").set("margin", "10px");
        accountLink.add(accountIcon);
        accountLink.add("Account");
        accountLink.addClassName("drawer-link");

        RouterLink manageLink = new RouterLink("", SuspendView.class);
        Icon manageIcon = new Icon(VaadinIcon.STOP);
        manageIcon.getStyle().set("width", "20px").set("height", "20px").set("margin", "10px");
        manageLink.add(manageIcon);
        manageLink.add("Suspensions");
        manageLink.addClassName("drawer-link");

        RouterLink purchaseHistoryLink = new RouterLink("", PurchaseHistoryView.class);
        Icon purchaseHistoryIcon = new Icon(VaadinIcon.ARCHIVE);
        purchaseHistoryIcon.getStyle().set("width", "20px").set("height", "20px").set("margin", "10px");
        purchaseHistoryLink.add(purchaseHistoryIcon);
        purchaseHistoryLink.add("Purchase History");
        purchaseHistoryLink.addClassName("drawer-link");

        drawerLinks.add(marketLink);
        drawerLinks.add(storesLink);
        drawerLinks.add(mystoresLink);
        drawerLinks.add(cartLink);
        drawerLinks.add(accountLink);
        drawerLinks.add(manageLink);
        drawerLinks.add(purchaseHistoryLink);

        VerticalLayout drawer = new VerticalLayout(marketLink, storesLink);

        if(VaadinSession.getCurrent().getAttribute("current-user") != null){
            drawer.add(mystoresLink);
        }

        drawer.add(cartLink);

        if(VaadinSession.getCurrent().getAttribute("current-user") != null){
            drawer.add(accountLink);
        }

        if(VaadinSession.getCurrent().getAttribute("current-user") != null){
            drawer.add(purchaseHistoryLink);
        }


        var isManager = VaadinSession.getCurrent().getAttribute("isManager");

        if(isManager != null && (Boolean) isManager){
            drawer.add(manageLink);
        }

        drawer.setWidth("200px");
        drawer.addClassName("drawer-links");

        add(drawer);

    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        String route = event.getLocation().getPath();

        // Get the session attribute to check if this is a sub-route
        String topLevelRoute = (String) VaadinSession.getCurrent().getAttribute("topLevelRoute");

        // Only update drawer links if this is a sub-route of HomeView
        if ("".equals(topLevelRoute)) {

            drawerLinks.forEach(link -> {
                if (link.getHref().equals(route)) {
                    link.getElement().getClassList().add("active");
                } else {
                    link.getElement().getClassList().remove("active");
                }
            });
        }
    }
}

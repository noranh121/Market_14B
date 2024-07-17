package org.market.PresentationLayer.views;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.views.components.PurchaseHistory;

@Route(value = "purchase-history", layout = HomeView.class)
@RoutePrefix("dash")
public class PurchaseHistoryView extends VerticalLayout implements BeforeEnterObserver{

    public PurchaseHistoryView() {
        PurchaseHistory history = new PurchaseHistory(-1);
        add(history);
        addClassName("purchase-view");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String username = (String) VaadinSession.getCurrent().getAttribute("current-user");

        if (username == null) {
            event.rerouteTo("login");
        }
    }

}

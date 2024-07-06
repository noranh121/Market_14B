package org.market.PresentationLayer.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "account", layout = HomeView.class)
@RoutePrefix("dash")
@AnonymousAllowed
public class Account extends VerticalLayout {
    public Account(){
        add(new H2("Account page"));
    }
}

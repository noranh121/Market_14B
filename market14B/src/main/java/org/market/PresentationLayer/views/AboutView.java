package org.market.PresentationLayer.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "about", layout =  MainLayout.class)
@AnonymousAllowed
public class AboutView extends VerticalLayout {
    public AboutView() {
        // VerticalLayout settings
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        // Header
        H1 header = new H1("About Us");
        header.addClassName("about-header");

        // Description
        Div description = new Div();
        description.addClassName("about-description");
        description.setText("Welcome to our application. This is a simple About page.");

        // Add components to the layout
        add(header, description);
    }
}

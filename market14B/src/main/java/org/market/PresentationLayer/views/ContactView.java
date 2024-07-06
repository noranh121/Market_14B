package org.market.PresentationLayer.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "contact", layout =  MainLayout.class)
public class ContactView extends VerticalLayout {

    public ContactView() {
        // VerticalLayout settings
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        // Header
        H1 header = new H1("Contact Us");
        header.addClassName("contact-header");

        // Contact Information
        Div contactInfo = new Div();
        contactInfo.addClassName("contact-info");
        contactInfo.setText("Email: contact@example.com\nPhone: +123456789");

        // Add components to the layout
        add(header, contactInfo);
    }
}

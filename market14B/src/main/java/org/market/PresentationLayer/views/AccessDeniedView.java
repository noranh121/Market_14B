package org.market.PresentationLayer.views;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("access-denied")
@AnonymousAllowed
public class AccessDeniedView extends VerticalLayout {
    public AccessDeniedView() {
        add(new Span("Access Denied. You do not have permission to view this page."));
    }
}

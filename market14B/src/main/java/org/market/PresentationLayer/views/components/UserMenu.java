package org.market.PresentationLayer.views.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.market.PresentationLayer.presenter.LogoutPresenter;

public class UserMenu extends Div {

    private Button logoutButton;
    private LogoutPresenter presenter;
    
    public UserMenu(String username) {
        addClassName("user-menu");

        // Welcome message
        Span welcomeMessage = new Span("Welcome, " + username);
        welcomeMessage.addClassName("welcome-message");

        // Logout button
        this.logoutButton = new Button("Logout");
        logoutButton.addClassName("logout-button");
        logoutButton.setIcon(VaadinIcon.SIGN_OUT.create());

        // Circular icon placeholder
        Avatar avatar = new Avatar(username);
        avatar.getElement().getStyle().set("--vaadin-avatar-size", "55px");
        avatar.setColorIndex(5);

        // Left column: Vertical layout with welcome message and logout button
        VerticalLayout leftColumn = new VerticalLayout();
        leftColumn.add(welcomeMessage, logoutButton);
        leftColumn.setSpacing(false);
        leftColumn.setPadding(false);
        leftColumn.setMargin(false);
        leftColumn.getStyle().set("align-items", "center");

        // Right column: Icon aligned with the left column
        HorizontalLayout rightColumn = new HorizontalLayout(avatar);
        rightColumn.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, avatar);
        rightColumn.setPadding(false);
        rightColumn.setMargin(false);

        // Main horizontal layout for left and right columns
        HorizontalLayout mainLayout = new HorizontalLayout(leftColumn, rightColumn);
        mainLayout.setPadding(false);
        mainLayout.setMargin(false);
        mainLayout.setSpacing(true);

        add(mainLayout);

        presenter = new LogoutPresenter(this);
    }

    public void addLogoutEventListener(ComponentEventListener<ClickEvent<Button>> eventListener) {
        this.logoutButton.addClickListener(eventListener);
    }
}

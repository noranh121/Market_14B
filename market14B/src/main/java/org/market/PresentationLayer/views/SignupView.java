package org.market.PresentationLayer.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.market.PresentationLayer.presenter.SignupPresenter;

@Route("signup")
@PageTitle("Market Signup")
public class SignupView extends VerticalLayout {

    private TextField username = new TextField("Username");
    private PasswordField password = new PasswordField("Password");
    private Button signupButton = new Button("Sign Up");

    private SignupPresenter presenter;

    public SignupView() {
        // Set the SignupView layout to use Flexbox and center the form
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        FormLayout layout = new FormLayout();
        // Form title
        H2 title = new H2("Sign Up");
        title.getStyle().set("text-align", "center");

        // Configure username field
        username.setRequiredIndicatorVisible(true);
        username.setMinLength(2);
        username.setMaxLength(20);
        username.setErrorMessage("Username must be between 4 and 20 characters");
        username.setPlaceholder("Enter your username");
        username.setClearButtonVisible(true);

        // Configure password field
        password.setRequiredIndicatorVisible(true);
        password.setMinLength(6);
        password.setErrorMessage("Password must be at least 6 characters long");
        password.setPlaceholder("Enter your password");
        password.setClearButtonVisible(true);

        // Add components to the form
        layout.add(title, username, password, signupButton);
        layout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        // Add the form layout to the center of the page
        add(layout);

        // Set maximum width of the form
        layout.setMaxWidth("400px");
        layout.getStyle().set("margin", "0 auto");
        add(new RouterLink("Login", LoginView.class));

        // Initialize the presenter
        this.presenter = new SignupPresenter(this);
    }

    public void addSignupButtonEventListener(ComponentEventListener<ClickEvent<Button>> event) {
        signupButton.addClickListener(event);
    }

    public TextField getUsername(){
        return username;
    }

    public PasswordField getPassword(){
        return password;
    }
}

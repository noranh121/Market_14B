package org.market.PresentationLayer.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.market.PresentationLayer.presenter.SignupPresenter;
import org.springframework.web.client.RestTemplate;

@Route("signup")
@PageTitle("Market Signup")
@AnonymousAllowed
public class SignupView extends FormLayout {

    private TextField username = new TextField("Username");
    private PasswordField password = new PasswordField("Password");
    private Button signupButton = new Button("Sign Up");

    private SignupPresenter prensenter;

    private final RestTemplate restTemplate;

    public SignupView() {
        this.restTemplate = new RestTemplate();

        // Form title
        H2 title = new H2("Sign Up");
        title.getStyle().set("text-align", "center");

        // Configure username field
        username.setRequiredIndicatorVisible(true);
        username.setMinLength(4);
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
        add(title, username, password, signupButton);
        setResponsiveSteps(new ResponsiveStep("0", 1));
        setMaxWidth("400px");
        getStyle().set("margin", "auto"); // Center horizontally and vertically

        this.prensenter = new SignupPresenter(this);
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
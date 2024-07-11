package org.market.PresentationLayer.views;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.market.PresentationLayer.presenter.LoginPresenter;

@Route("login")
@PageTitle("Market Login")
public class LoginView extends VerticalLayout {

    private final LoginForm loginForm;
    private LoginPresenter prensenter;

    public LoginView() {
        // VerticalLayout settings
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidthFull();
        setHeightFull();

        // Create a login form
        loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(false); // Disable "Forgot password?" link

        // Add login form to the layout
        add(loginForm);
        add(new RouterLink("sign up", SignupView.class));

        // Initializing the presenter
        this.prensenter = new LoginPresenter(this);
    }

    public void addLoginButtonEventListener(ComponentEventListener<AbstractLogin.LoginEvent> listener){
        loginForm.addLoginListener(listener);
    }

    public void NavigateToHomepage(){
        this.getUI().ifPresent(ui -> ui.navigate("/"));
    }

    public void showSuccessNotification(String message) {
        Notification notification = new Notification(message, 3000);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
    }

    public void showErrorNotification(String message) {
        Notification notification = new Notification(message, 3000);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

    public void enableLogin() {
        this.loginForm.setEnabled(true);
    }
}

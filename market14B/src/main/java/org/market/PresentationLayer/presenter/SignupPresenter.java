package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.market.PresentationLayer.views.SignupView;
import org.market.ServiceLayer.Response;
import org.market.Web.Requests.ReqUser;
import org.springframework.web.client.RestTemplate;

public class SignupPresenter {

    private SignupView view;
    private RestTemplate restTemplate;

    public SignupPresenter(SignupView signupView){
        this.view = signupView;
        this.restTemplate = new RestTemplate();
        initView();
    }

    public void initView(){
        this.view.addSignupButtonEventListener(e -> {
            onSignupButtonClicked(view.getUsername(), view.getPassword());
        });
    }

    public void onSignupButtonClicked(TextField username, PasswordField password) {
            try {
                if (validateForm(username, password)) {

                    ReqUser request = new ReqUser();
                    request.setUsername(username.getValue());
                    request.setPassword(password.getValue());
                    request.setAge(24.0);
                    request.setGuest("nobody");

                    String registerUrl = "http://localhost:8080/api/users/register";

                    Response<String> response = restTemplate.postForObject(registerUrl, request, Response.class);

                    if (response != null && response.isError()) {
                        showErrorNotification("Error: " + response.getErrorMessage());
                    } else {
                        assert response != null;
                        showSuccessNotification(response.getValue());
                    }
                }
            }catch(Exception ex){
                showErrorNotification("Error: " + ex.getMessage());
            }
    }

    private boolean validateForm(TextField username, PasswordField password) {
        boolean isValid = true;

        if (username.isEmpty()) {
            username.setInvalid(true);
            username.setErrorMessage("Username is required");
            isValid = false;
        }

        if (password.isEmpty()) {
            password.setInvalid(true);
            password.setErrorMessage("Password is required");
            isValid = false;
        }

        return isValid;
    }

    private void showSuccessNotification(String message) {
        Notification notification = new Notification(message, 3000);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
    }

    private void showErrorNotification(String message) {
        Notification notification = new Notification(message, 3000);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

}

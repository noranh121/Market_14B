package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.market.PresentationLayer.handlers.ErrorHandler;
import org.market.PresentationLayer.views.SignupView;
import org.market.Web.Requests.ReqUser;
import org.springframework.web.client.HttpClientErrorException;
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

                    String response = restTemplate.postForObject(registerUrl, request, String.class);

                    ErrorHandler.showSuccessNotification(response);
                }
            }catch(HttpClientErrorException e){
                ErrorHandler.handleError(e, ()->{});
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

}

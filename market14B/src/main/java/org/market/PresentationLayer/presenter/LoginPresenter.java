package org.market.PresentationLayer.presenter;

import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.handlers.PermissionHandler;
import org.market.PresentationLayer.models.AuthResponse;
import org.market.PresentationLayer.views.LoginView;
import org.market.Web.Requests.ReqUser;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class LoginPresenter {

    private LoginView view;
    private RestTemplate restTemplate;

    public LoginPresenter(LoginView loginView){
        this.view = loginView;
        this.restTemplate = new RestTemplate();
        initView();
    }

    public void initView(){
        this.view.addLoginButtonEventListener(e -> {
            onLoginButtonClicked(e.getUsername(), e.getPassword());
        });
    }

    public void onLoginButtonClicked(String username, String password) {

        if (validateInput(username, password)) {
            try {
                String guest = VaadinSession.getCurrent().getAttribute("guest").toString();

                ReqUser authRequest = new ReqUser();
                authRequest.setUsername(username);
                authRequest.setPassword(password);
                authRequest.setGuest(guest);
                String loginUrl = "http://localhost:8080/api/users/login";

                AuthResponse response = restTemplate.postForObject(loginUrl, authRequest, AuthResponse.class);

                assert response != null;

                VaadinSession.getCurrent().setAttribute("current-user", username);
                VaadinSession.getCurrent().setAttribute("access-token", response.getAccess_token());
                VaadinSession.getCurrent().setAttribute("refresh-token", response.getRefresh_token());

                System.out.println(response.getAccess_token());
                System.out.println(response.getRefresh_token());

                view.showSuccessNotification("Successful Login");

                // load user permissions
                PermissionHandler.loadPermissions(username);

                view.NavigateToHomepage();
                
            } catch (Exception e) {
                view.showErrorNotification(e.getMessage());
            }
        } else {
            view.showErrorNotification("Incorrect Username or Password!");
        }
    }

    private boolean validateInput(String username, String password) {
        return username != null && !username.isEmpty() && password != null && !password.isEmpty();
    }

    private HttpHeaders createAuthHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }
}

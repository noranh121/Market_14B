package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.views.components.UserMenu;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class LogoutPresenter {

    public UserMenu view;
    private RestTemplate restTemplate;


    public LogoutPresenter(UserMenu menuView){
        this.view = menuView;
        this.restTemplate = new RestTemplate();
        initView();
    }

    private void initView() {
        this.view.addLogoutEventListener(e -> {
            if(VaadinSession.getCurrent().getAttribute("current-user") != null) {
                logoutUser(VaadinSession.getCurrent().getAttribute("current-user").toString());
                VaadinSession.getCurrent().setAttribute("current-user", null);
                VaadinSession.getCurrent().setAttribute("access-token", null);
                VaadinSession.getCurrent().setAttribute("refresh-token", null);
                VaadinSession.getCurrent().setAttribute("guest-user", null);
                VaadinSession.getCurrent().setAttribute("permissions", null);
                UI.getCurrent().navigate("/");
                UI.getCurrent().getPage().reload();
            }
        });
    }

    private void logoutUser(String username) {
        try {
            String url = "http://localhost:8080/api/users/logout/{username}";

            // Set up the headers (optional, depending on your API requirements)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the request entity with headers (if you need to send a request body, include it here)
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            // Make the POST request
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class, username);

            // Get the response body
            System.out.println(responseEntity.getBody());
        }
        catch (HttpClientErrorException e){

        }
    }
}

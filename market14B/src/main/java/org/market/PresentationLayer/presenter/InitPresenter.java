package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.notifications.NotificationHandler;
import org.market.PresentationLayer.views.MainLayout;
import org.market.ServiceLayer.Response;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class InitPresenter {

    private RestTemplate restTemplate;
    private MainLayout view;
    private NotificationHandler notificationHandler;


    public InitPresenter(MainLayout mainView){
        this.view = mainView;
        this.restTemplate = new RestTemplate();
        initView();
    }

    private void initView() {
        this.notificationHandler = new NotificationHandler(UI.getCurrent());
        handleOnAttach("ws://localhost:8080/notifications", VaadinSession.getCurrent().getAttribute("current-user"));
    }

    public void handleOnAttach(String serverUri, Object username) {
        if(username != null){
            notificationHandler.connect(serverUri, username.toString());
        }
        else{
            connectGuest();
        }
    }

    public void handleOnDetach() {
        if (notificationHandler != null) {
            notificationHandler.disconnect();
        }
        if(VaadinSession.getCurrent().getAttribute("current-user") == null){
            disconnectGuest();
        }
    }

    private void connectGuest() {
        String url = "http://localhost:8080/api/users/enter-as-guest/{age}";

        // Set up the headers (optional, depending on your API requirements)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity with headers (if you need to send a request body, include it here)
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Make the POST request
        ResponseEntity<Response> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class, 24.0);

        // Get the response body
        System.out.println(responseEntity.getBody().getValue());
    }

    private void disconnectGuest() {
        String url = "http://localhost:8080/api/users/guest-exit/{username}";

        // Set up the headers (optional, depending on your API requirements)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity with headers (if you need to send a request body, include it here)
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Make the POST request
        ResponseEntity<Response> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class, "nobody");

        // Get the response body
        System.out.println(responseEntity.getBody().getValue());
    }
}

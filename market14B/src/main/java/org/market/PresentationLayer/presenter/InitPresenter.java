package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.notifications.NotificationHandler;
import org.market.PresentationLayer.views.MainLayout;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
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
            //String guest = (String) VaadinSession.getCurrent().getAttribute("guest-user");
            connectGuest();
        }
    }

    public void handleOnDetach() {
        if (notificationHandler != null) {
            notificationHandler.disconnect();
        }
//        if(VaadinSession.getCurrent().getAttribute("current-user") == null){
//            disconnectGuest();
//        }
    }

    private void connectGuest() {
        try {
            String url = "http://localhost:8080/api/users/enter-as-guest/{age}";

            // Set up the headers (optional, depending on your API requirements)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the request entity with headers (if you need to send a request body, include it here)
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            // Make the POST request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class, 24.0);

            VaadinSession.getCurrent().setAttribute("guest-user", response.getBody());
            // Get the response body
        }catch(HttpClientErrorException e){

        }
    }

    private void disconnectGuest() {
        try {

            String guest = (String) VaadinSession.getCurrent().getAttribute("guest-user");

            if (guest != null) {
                String url = "http://localhost:8080/api/users/guest-exit/{username}";

                // Set up the headers (optional, depending on your API requirements)
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                // Create the request entity with headers (if you need to send a request body, include it here)
                HttpEntity<String> requestEntity = new HttpEntity<>(headers);

                // Make the POST request
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class, guest);

                System.out.println(response.getBody());
            }
        }catch (HttpClientErrorException e){

        }
    }
}

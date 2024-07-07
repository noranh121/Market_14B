package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.errors.ErrorHandler;
import org.market.PresentationLayer.views.MyStoresView;
import org.market.ServiceLayer.Response;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class MyStoresPresenter {

    private MyStoresView view;
    private RestTemplate restTemplate;

    public MyStoresPresenter(MyStoresView myStoresView){
        this.view = myStoresView;
        this.restTemplate = new RestTemplate();
        initView();
    }

    public void initView(){
        this.view.setAddStoreButtonEventListener(e ->
            onAddStoreButtonClicked());
    }

    public void onAddStoreButtonClicked() {
        Runnable originalRequest = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("run");

                    String accessToken = (String) VaadinSession.getCurrent().getAttribute("access-token");
                    String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
                    String addStoreUrl = "http://localhost:8080/api/stores/add-store/username={username}&desc={desc}";

                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(accessToken);
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

                    ResponseEntity<Response> response = restTemplate.exchange(addStoreUrl, HttpMethod.POST, requestEntity, Response.class, username, "This is description!");

                    Notification.show(response.getBody().getValue().toString(), 3000, Notification.Position.MIDDLE);

                } catch (HttpClientErrorException e) {
                    ErrorHandler.handleError(e, this);
                }
            }
        };

        // Execute the original request
        originalRequest.run();
    }
}

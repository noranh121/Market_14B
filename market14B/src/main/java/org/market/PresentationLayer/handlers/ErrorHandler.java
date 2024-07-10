package org.market.PresentationLayer.handlers;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.models.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ErrorHandler {

    public static void handleError(HttpClientErrorException e, Runnable originalRequest) {
        System.out.println(HttpStatus.UNAUTHORIZED);
        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            handleUnauthorizedError(originalRequest);
        } else {
            showErrorNotification(e.getMessage());
        }
    }

    private static void handleUnauthorizedError(Runnable originalRequest) {
        // Attempt to refresh the token and retry the request
        if (refreshAccessToken()) {
            // Retry the original request
            originalRequest.run();
        } else {
            Notification.show("Session expired. Please log in again.", 3000, Notification.Position.MIDDLE);
            // Redirect to the login page
            VaadinSession.getCurrent().setAttribute("current-user", null);
            VaadinSession.getCurrent().setAttribute("access-token", null);
            VaadinSession.getCurrent().setAttribute("refresh-token", null);

            UI.getCurrent().getPage().reload();
        }
    }

    private static boolean refreshAccessToken() {
        String refreshToken = (String) VaadinSession.getCurrent().getAttribute("refresh-token");

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<AuthResponse> response = restTemplate.postForEntity("http://localhost:8080/api/users/refresh-token", refreshToken, AuthResponse.class);
            AuthResponse authResponse = response.getBody();

            if (authResponse != null) {
                VaadinSession.getCurrent().setAttribute("access-token", authResponse.getAccess_token());
                return true;
            }
        } catch (HttpClientErrorException e) {
        }
        return false;
    }

    public static void showErrorNotification(String message) {
        Notification notification = new Notification(message, 3000);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

    public static void showSuccessNotification(String message) {
        Notification notification = new Notification(message, 3000);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
    }
}

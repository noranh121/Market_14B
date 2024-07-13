package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.handlers.ErrorHandler;
import org.market.PresentationLayer.views.SuspendView;
import org.market.Web.Requests.SuspendReq;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class SuspendPresenter {

    private SuspendView view;
    private RestTemplate restTemplate;

    public SuspendPresenter(SuspendView suspendView){
        this.view = suspendView;
        this.restTemplate = new RestTemplate();
        initView();
    }

    public void initView() {
        this.view.setSuspendClickEventListener(e -> {
            onSuspendButtonClick(view.getUserField(), view.getTimeField(), view.getIndefinitely(), view.getTemporary());
            loadSuspendedUsers();
        });
        this.view.setResumeClickEventListener(e -> {
            onResumeButtonClick(view.getUserField(), view.getTimeField(), view.getIndefinitely(), view.getTemporary());
            loadSuspendedUsers();
        });
    }

    private void onSuspendButtonClick(TextField userField, IntegerField timeField, Checkbox indefinitely, Checkbox temporary) {
        if(validateSuspension(userField, timeField) && (indefinitely.getValue() || temporary.getValue())) {
            try {
                String suspendIndefinitely = "http://localhost:8080/api/users/suspend-user-indefinitely";
                String suspendTemporary = "http://localhost:8080/api/users/suspend-user-temporarily";
                String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
                String url = indefinitely.getValue() ? suspendIndefinitely : suspendTemporary;

                SuspendReq request = new SuspendReq();
                request.setManager(username);
                request.setUsername(userField.getValue());
                request.setDuration(timeField.getValue() != null ? timeField.getValue() : 0);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<SuspendReq> requestEntity = new HttpEntity<>(request, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                view.createSuspendedUsersLayout(response.getBody());

                ErrorHandler.showSuccessNotification("Successfully suspended " + userField.getValue());

            } catch (HttpClientErrorException e) {
                ErrorHandler.handleError(e, () -> {
                });
            }
        }
    }

    private void onResumeButtonClick(TextField userField, IntegerField timeField, Checkbox indefinitely, Checkbox temporary) {
        if(validateSuspension(userField, timeField) && (indefinitely.getValue() || temporary.getValue())) {
            try {
                String resumeIndefinitely = "http://localhost:8080/api/users/resume-user-indefinitely";
                String resumeTemporary = "http://localhost:8080/api/users/resume-user-temporarily";
                String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
                String url = indefinitely.getValue() ? resumeIndefinitely : resumeTemporary;

                SuspendReq request = new SuspendReq();
                request.setManager(username);
                request.setUsername(userField.getValue());
                request.setDuration(timeField.getValue() != null ? timeField.getValue() : 0);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<SuspendReq> requestEntity = new HttpEntity<>(request, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                ErrorHandler.showSuccessNotification("Successfully suspended " + userField.getValue());

            } catch (HttpClientErrorException e) {
                ErrorHandler.handleError(e, () -> {
                });
            }
        }
    }

    public void loadSuspendedUsers() {
        try {
            String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
            String url = "http://localhost:8080/api/users/suspended-users/{manager}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, username);

            view.createSuspendedUsersLayout(response.getBody());

        } catch (HttpClientErrorException e) {
            ErrorHandler.handleError(e, () -> {
            });
        }
    }

    private boolean validateSuspension(TextField username, IntegerField duration) {
        boolean isValid = true;

        if (username.isEmpty()) {
            username.setInvalid(true);
            username.setErrorMessage("Username is required");
            isValid = false;
        }
        if (duration.isEmpty() && view.getTemporary().getValue()) {
            duration.setInvalid(true);
            duration.setErrorMessage("Duration is required");
            isValid = false;
        }
        return isValid;
    }
}

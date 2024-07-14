package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.handlers.ErrorHandler;
import org.market.PresentationLayer.handlers.PermissionHandler;
import org.market.PresentationLayer.views.ManagerSettingsView;
import org.market.Web.DTOS.StoreDTO;
import org.market.Web.Requests.PermissionReq;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ManagerSettingPresenter {

    private ManagerSettingsView view;
    private RestTemplate restTemplate;

    public ManagerSettingPresenter(ManagerSettingsView settingsView){
        this.view = settingsView;
        this.restTemplate = new RestTemplate();
        initView();
    }

    private void initView() {
        PermissionHandler.loadPermissions();
        StoreDTO store = getStore(this.view.getStore_id());
        view.setStore(store);
        view.createTopBar();
        view.createAssignRoleSection("Assign Role");
        view.createPermissionSection("Permissions");
        view.createDiscountSection("Discount Policies");
        view.createPolicySection("Purchase Policies");
        // loadPolicies();

        view.setAssignRoleClickEventListener(e ->
                onAssignRoleClick(view.getStore_id(), view.getRoleField(), view.getIsOwner().getValue(),
                        view.getIsManager().getValue(),
                        new Boolean[]{view.getEditProducts().getValue(), view.getEditPurchaseHistory().getValue(),
                                view.getEditDiscountHistory().getValue()})
        );

        view.setUpdateRoleClickEventListener(e ->
                onUpdateRoleClick(view.getStore_id(), view.getRoleField(), view.getIsOwner().getValue(),
                        view.getIsManager().getValue(),
                        new Boolean[]{view.getEditProducts().getValue(), view.getEditPurchaseHistory().getValue(),
                                view.getEditDiscountHistory().getValue()})
        );

        view.setUnassignRoleClickEventListener(e ->
                onUnassignRoleClicked(view.getStore_id(), view.getRoleField(), view.getIsOwner().getValue(),
                        view.getIsManager().getValue(),
                        new Boolean[]{view.getEditProducts().getValue(), view.getEditPurchaseHistory().getValue(),
                                view.getEditDiscountHistory().getValue()})
        );

        view.setResignRoleClickEventListener(e ->
                onResignRoleClicked(view.getStore_id(), view.getRoleField(), view.getIsOwner().getValue(),
                        view.getIsManager().getValue(),
                        new Boolean[]{view.getEditProducts().getValue(), view.getEditPurchaseHistory().getValue(),
                                view.getEditDiscountHistory().getValue()})
        );

        view.addDiscountClickEventListener(e ->
                onAddDiscountClicked()
        );

        view.addPurchaseClickEventListener(e ->
                onAddPurchaseClicked()
        );
    }

    private void onAssignRoleClick(int store_id, TextField assignee, boolean isOwner, boolean isManager, Boolean[] PType) {
        if(validateAssignRole(assignee) && (isManager || isOwner)) {
            try {
                String ownerURL = "http://localhost:8080/api/users/assign-store-owner";
                String managerURL = "http://localhost:8080/api/users/assign-store-manager";
                String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
                String url = isOwner ? ownerURL : managerURL;

                PermissionReq request = new PermissionReq(store_id, username, assignee.getValue(), isOwner,isManager,PType);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<PermissionReq> requestEntity = new HttpEntity<>(request, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                ErrorHandler.showSuccessNotification("Successfully assigned " + assignee.getValue());

            } catch (HttpClientErrorException e) {
                ErrorHandler.handleError(e, () -> {
                });
            }
        }
    }

    private void onUpdateRoleClick(int store_id, TextField assignee, boolean isOwner, boolean isManager, Boolean[] PType) {
        if(validateAssignRole(assignee) && (isManager || isOwner)) {
            try {
                String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
                String url = "http://localhost:8080/api/users/edit-permissions";

                PermissionReq request = new PermissionReq(store_id, username, assignee.getValue(), isOwner,isManager,PType);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<PermissionReq> requestEntity = new HttpEntity<>(request, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                ErrorHandler.showSuccessNotification("Successfully updated " + assignee.getValue() + " permissions");

            } catch (HttpClientErrorException e) {
                ErrorHandler.handleError(e, () -> {
                });
            }
        }
    }

    private void onUnassignRoleClicked(int store_id, TextField assignee, boolean isOwner, boolean isManager, Boolean[] PType) {
        if(validateAssignRole(assignee)) {
            try {
                String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
                String url = "http://localhost:8080/api/users/unassign-user";

                PermissionReq request = new PermissionReq(store_id, username, assignee.getValue(), isOwner,isManager,PType);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<PermissionReq> requestEntity = new HttpEntity<>(request, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                ErrorHandler.showSuccessNotification("Successfully unassigned " + assignee.getValue());

            } catch (HttpClientErrorException e) {
                ErrorHandler.handleError(e, () -> {
                });
            }
        }
    }

    private void onResignRoleClicked(int store_id, TextField assignee, boolean isOwner, boolean isManager, Boolean[] PType) {
        try {
            String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
            String url = "http://localhost:8080/api/users/resign";

            PermissionReq request = new PermissionReq(store_id, username, assignee.getValue(), isOwner,isManager,PType);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PermissionReq> requestEntity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            ErrorHandler.showSuccessNotification("Successfully resigned from store");

            UI.getCurrent().navigate("dash/mystores");

        } catch (HttpClientErrorException e) {
            ErrorHandler.handleError(e, () -> {
            });
        }
    }

    private void onAddDiscountClicked() {

    }

    private void onAddPurchaseClicked() {

    }

    private boolean validateAddDiscount() {
        return false;
    }


    private boolean validateAssignRole(TextField assignee) {
        boolean isValid = true;

        if (assignee.isEmpty()) {
            assignee.setInvalid(true);
            assignee.setErrorMessage("Username is required");
            isValid = false;
        }
        return isValid;
    }

    private StoreDTO getStore(int store_id) {
        try{
            String getStoreUrl = "http://localhost:8080/api/stores/store/{store_id}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<StoreDTO> response = restTemplate.exchange(getStoreUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<StoreDTO>() {}, store_id);

            return response.getBody();

        }catch (HttpClientErrorException e){
            ErrorHandler.handleError(e, ()->{});
            return null;
        }
    }
}

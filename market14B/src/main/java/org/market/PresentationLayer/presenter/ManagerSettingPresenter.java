package org.market.PresentationLayer.presenter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.handlers.ErrorHandler;
import org.market.PresentationLayer.handlers.PermissionHandler;
import org.market.PresentationLayer.views.ManagerSettingsView;
import org.market.Web.DTOS.StoreDTO;
import org.market.Web.Requests.PermissionReq;
import org.market.Web.Requests.addDiscountReq;
import org.market.Web.Requests.addPurchaseReq;
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
        view.createHistorySection();
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
        if(validateAddDiscount()){
            try {
                String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
                String addLogicalUrl = "http://localhost:8080/api/stores/add-logical-discount";
                String addCategoryUrl = "http://localhost:8080/api/stores/add-category-discount";
                String addProductUrl = "http://localhost:8080/api/stores/add-product-discount";
                String addStoreUrl = "http://localhost:8080/api/stores/add-store-discount";

                String url = !view.getSelectdp().getValue().equals("none") ? addLogicalUrl :
                                view.getDpolicyType().getValue().equals("Product") ? addProductUrl :
                                view.getDpolicyType().getValue().equals("Store") ? addStoreUrl :
                                addCategoryUrl;

                addDiscountReq request = new addDiscountReq();
                request.setUsername(username);
                request.setPercentage(view.getDiscountPercentage().getValue());
                request.setProductName(view.getProductNamedp().getValue());
                request.setCategoryName(view.getCategoryNamedp().getValue());
                request.setPrice(view.getMinPrice().getValue());
                request.setQuantity(view.getMinQuantity().getValue());
                request.setStoreId(view.getStore_id());
                request.setLogicalRule(view.getSelectdp().getValue());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<addDiscountReq> requestEntity = new HttpEntity<>(request, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                ErrorHandler.showSuccessNotification("Successfully added discount policy");

            } catch (HttpClientErrorException e) {
                ErrorHandler.handleError(e, () -> {
                });
            }
        }
    }

    private void onAddPurchaseClicked() {
        if(validateAddPurchase()){
            try {
                String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
                String addLogicalUrl = "http://localhost:8080/api/stores/add-logical-purchase";
                String addUserUrl = "http://localhost:8080/api/stores/add-user-purchase";
                String addCategoryUrl = "http://localhost:8080/api/stores/add-category-purchase";
                String addProductUrl = "http://localhost:8080/api/stores/add-product-purchase";
                String addShoppingCartUrl = "http://localhost:8080/api/stores/add-shoppingcart-purchase";

                String url = !view.getSelectpp().getValue().equals("none") ? addLogicalUrl :
                        view.getPpolicyType().getValue().equals("Product") ? addProductUrl :
                                        view.getPpolicyType().getValue().equals("User") ? addUserUrl :
                                                view.getPpolicyType().getValue().equals("Shopping Cart") ? addShoppingCartUrl :
                                                        addCategoryUrl;

                addPurchaseReq request = new addPurchaseReq();
                request.setUsername(username);
                request.setWeight(!view.getWeight().isEmpty() ? view.getWeight().getValue() : 0);
                request.setProductName(view.getProductNamepp().getValue());
                request.setCategoryName(view.getCategoryNamepp().getValue());
                request.setPrice(!view.getPrice().isEmpty() ? view.getPrice().getValue() : 0);
                request.setQuantity(!view.getQuantity().isEmpty() ? view.getQuantity().getValue() : 0);
                request.setAge(!view.getAge().isEmpty() ? view.getAge().getValue() : 0);
                request.setDate(view.getDate().getValue());
                request.setLogicalRule(view.getSelectdp().getValue());
                request.setAtLeast(view.getAtLeast().getValue().equals("min") ? 1 : 0);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<addPurchaseReq> requestEntity = new HttpEntity<>(request, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                ErrorHandler.showSuccessNotification("Successfully added purchase policy");

            } catch (HttpClientErrorException e) {
                ErrorHandler.handleError(e, () -> {
                });
            }
        }
    }

    private boolean validateAddDiscount() {
        boolean isValid = true;

        if(view.getSelectdp().getValue().equals("none")) {
            if (view.getDpolicyType().getValue().equals("Product") && view.getProductNamedp().isEmpty()) {
                view.getProductNamedp().setInvalid(true);
                isValid = false;
            }
            if (view.getDpolicyType().getValue().equals("Category") && view.getCategoryNamedp().isEmpty()) {
                view.getCategoryNamedp().setInvalid(true);
                isValid = false;
            }
        }
        if (view.getDiscountPercentage().isEmpty()) {
            view.getDiscountPercentage().setInvalid(true);
            isValid = false;
        }
        return isValid;
    }

    private boolean validateAddPurchase() {
        boolean isValid = true;

        if(view.getSelectdp().getValue().equals("none")) {
            if (view.getPpolicyType().getValue().equals("Product") && view.getProductNamepp().isEmpty()) {
                view.getProductNamepp().setInvalid(true);
                isValid = false;
            }
            if (view.getPpolicyType().getValue().equals("Category") && view.getCategoryNamepp().isEmpty()) {
                view.getCategoryNamepp().setInvalid(true);
                isValid = false;
            }
        }
        if (view.getPolicyOn().getValue().equals("Quantity") && view.getQuantity().isEmpty()) {
            view.getQuantity().setInvalid(true);
            isValid = false;
        }
        if (view.getPolicyOn().getValue().equals("Age") && view.getAge().isEmpty()) {
            view.getAge().setInvalid(true);
            isValid = false;
        }
        if (view.getPolicyOn().getValue().equals("Weight") && view.getWeight().isEmpty()) {
            view.getWeight().setInvalid(true);
            isValid = false;
        }
        if (view.getPolicyOn().getValue().equals("Price") && view.getPrice().isEmpty()) {
            view.getPrice().setInvalid(true);
            isValid = false;
        }
        if (view.getDate().isEmpty()) {
            view.getDate().setInvalid(true);
            isValid = false;
        }
        return isValid;
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

package org.market.PresentationLayer.handlers;

import com.vaadin.flow.server.VaadinSession;
import org.market.Web.DTOS.PermissionDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class PermissionHandler {

    public static boolean hasPermission(int storeId, int type) {
        VaadinSession currentSession = VaadinSession.getCurrent();
        if (currentSession == null) {
            // Log this situation
            return false;
        }

        Object permissions = currentSession.getAttribute("permissions");
        if (permissions == null) {
            return false;
        }

        if (!(permissions instanceof List<?> permissionList)) {
            // Log the unexpected type
            return false;
        }

        if (permissionList.isEmpty() || !(permissionList.get(0) instanceof PermissionDTO)) {
            // Log the unexpected list content
            return false;
        }

        @SuppressWarnings("unchecked")
        List<PermissionDTO> permissionDTOList = (List<PermissionDTO>) permissionList;

        PermissionDTO permission = permissionDTOList.stream()
                .filter(e -> e.getStoreId() == storeId)
                .findFirst()
                .orElse(null);
        if (permission == null) {
            return false;
        }

        return permission.getPType()[type];
    }

    public static String getRole(int storeId) {
        VaadinSession currentSession = VaadinSession.getCurrent();
        if (currentSession == null) {
            // Log this situation
            return "User";
        }

        Object permissions = currentSession.getAttribute("permissions");
        if (permissions == null) {
            return "User";
        }

        if (!(permissions instanceof List<?> permissionList)) {
            // Log the unexpected type
            return "User";
        }

        if (permissionList.isEmpty() || !(permissionList.get(0) instanceof PermissionDTO)) {
            // Log the unexpected list content
            return "User";
        }

        @SuppressWarnings("unchecked")
        List<PermissionDTO> permissionDTOList = (List<PermissionDTO>) permissionList;

        PermissionDTO permission = permissionDTOList.stream()
                .filter(e -> e.getStoreId() == storeId)
                .findFirst()
                .orElse(null);
        if (permission == null) {
            return "User";
        }

        if(permission.getStoreOwner()){
            return "Owner";
        }
        else if(permission.getStoreManager()){
            return "Manager";
        }
        else {
            return "User";
        }
    }

    public static void loadPermissions() {
        String username = (String) VaadinSession.getCurrent().getAttribute("current-user");
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/api/users/get-permission/{username}";

        // Set up the headers (optional, depending on your API requirements)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity with headers (if you need to send a request body, include it here)
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<PermissionDTO>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<PermissionDTO>>(){}, username);

        VaadinSession.getCurrent().setAttribute("permissions", response.getBody());

        System.out.println("Successfully retrieved user's permissions");
    }

}

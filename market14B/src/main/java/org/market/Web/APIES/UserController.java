package org.market.Web.APIES;

import org.market.PresentationLayer.models.AuthResponse;
import org.market.ServiceLayer.Response;
import org.market.ServiceLayer.ServiceFactory;
import org.market.ServiceLayer.TokenService;
import org.market.Web.DTOS.PermissionDTO;
import org.market.Web.DTOS.StoreDTO;
import org.market.Web.Requests.PermissionReq;
import org.market.Web.Requests.ReqUser;
import org.market.Web.Requests.cartOp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    private ServiceFactory service;
    private TokenService jwtUtil;

    @Autowired
    public UserController(ServiceFactory service, TokenService jwtUtil){
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    // DONE
    @PostMapping("/refresh-token")
    public ResponseEntity<?> RefreshToken(@RequestBody String refresh_token){
        try{
            String username = jwtUtil.extractUsername(refresh_token);
            if(username != null && jwtUtil.validateToken(refresh_token,username)) {
                AuthResponse response = new AuthResponse();
                response.setAccess_token(jwtUtil.generateAccessToken(username));
                response.setRefresh_token(jwtUtil.generateRefreshToken(username));
                return ResponseEntity.ok(response);
            }
            else{
                service.Logout(jwtUtil.extractUsernameIgnoringExpiration(refresh_token));
                return ResponseEntity.status(401).build();
            }
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to refresh token");
        }

    }

    // DONE
    @PostMapping("/enter-as-guest/{age}")
    public ResponseEntity<?> enterAsGuest(@PathVariable Double age){
        try{
            String res = service.EnterAsGuest(age);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to add guest");
        }
    }

    // DONE
    @PostMapping("/guest-exit/{username}")
    public ResponseEntity<?> GuestExit(@PathVariable String username) {
        try{
            String res = service.GuestExit(username);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to remove guest");
        }
    }

    // DONE
    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody ReqUser user){
        try{
            String res =  service.Login(user.getGuest(), user.getUsername(), user.getPassword());
            if(res.equals("logged in successfully")){
                AuthResponse response = new AuthResponse();
                response.setAccess_token(jwtUtil.generateAccessToken(user.getUsername()));
                response.setRefresh_token(jwtUtil.generateRefreshToken(user.getUsername()));
                return ResponseEntity.ok(response);
            }
            else{
                return ResponseEntity.status(400).body(res);
            }
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to remove guest");
        }
    }

    // DONE
    @PostMapping("/logout/{username}")
    public ResponseEntity<?> Logout(@PathVariable String username){
        try{
            String res = service.Logout(username);
            return ResponseEntity.ok(Response.successRes(res));
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to logout");
        }
    }

    // DONE
    @PostMapping("/register")
    public ResponseEntity<?> Register(@RequestBody ReqUser user){
        try{
            Response<String> response = service.Register(user.getUsername(), user.getPassword(), user.getAge());
            if (response.isError()){
                return ResponseEntity.status(400).body(response.getErrorMessage());
            }
            else{
                return ResponseEntity.ok(response);
            }
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed to register");
        }
    }

    // TODO
    @PostMapping("/buy/{username}")
    public Response<String> Buy(@PathVariable String username) {
        return Response.failRes("Not implemented");
       // return service.Buy(username);
    }

    // TODO
    @PostMapping("/add-to-cart")
    public Response<String> addToCart(@RequestBody cartOp op){
        return service.addToCart(op.getUsername(), op.getProdID(), op.getStoreID(), op.getQuant());
    }

    // TODO
    @GetMapping("/inspect-cart/{username}")
    public Response<String> inspectCart(@PathVariable String username) {
        return service.inspectCart(username);
    }

    // TODO
    @PostMapping("/remove-cart-item")
    public Response<String> removeCartItem(@RequestBody cartOp op) {
        return service.removeCartItem(op.getUsername(), op.getStoreID(), op.getProdID());
    }

    // TODO
    @PostMapping("/edit-permissions")
    public ResponseEntity<?> EditPermissions(@RequestBody PermissionReq per) throws Exception{
        try{
            Response<String> res = service.EditPermissions(per.getStoreID(), per.getOwnerUserName(), per.getUsername(), per.getStoreOwner(), per.getStoreManager(), per.getpType());
            if(res.isError()){
                return ResponseEntity.status(404).body("Failed edit to user permissions");
            }
            else{
                return ResponseEntity.ok(res.getValue());
            }
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed to edit user permissions");
        }
    }

    // DONE
    @PostMapping("/assign-store-manager")
    public ResponseEntity<?> AssignStoreManager(@RequestBody PermissionReq per) throws Exception{
        try{
            Response<String> res = service.AssignStoreManager(per.getStoreID(), per.getOwnerUserName(), per.getUsername(), per.getpType());
            if(res.isError()){
                return ResponseEntity.status(404).body("Failed to assign user");
            }
            else{
                return ResponseEntity.ok(res.getValue());
            }
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed to assign user");
        }
    }

    // DONE
    @PostMapping("/assign-store-owner")
    public ResponseEntity<?> AssignStoreOnwer(@RequestBody PermissionReq per) throws Exception{
        try{
            Response<String> res = service.AssignStoreOnwer(per.getStoreID(), per.getOwnerUserName(), per.getUsername(), per.getpType());
            if(res.isError()){
                return ResponseEntity.status(404).body("Failed to assign user");
            }
            else{
                return ResponseEntity.ok(res.getValue());
            }
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed to assign user");
        }
    }

    // TODO
    @PostMapping("/unassign-user")
    public ResponseEntity<?> unassignUser(@RequestBody PermissionReq per) throws Exception{
        try{
            Response<String> res = service.unassignUser(per.getStoreID(), per.getOwnerUserName(), per.getUsername());
            if(res.isError()){
                return ResponseEntity.status(404).body("Failed to unassign user");
            }
            else{
                return ResponseEntity.ok(res.getValue());
            }
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed to unassign user");
        }
    }

    @PostMapping("/resign")
    public ResponseEntity<?> resign(@RequestBody PermissionReq per) throws Exception{
        try{
            Response<String> res = service.resign(per.getStoreID(), per.getOwnerUserName());
            if(res.isError()){
                return ResponseEntity.status(404).body("Failed to resign");
            }
            else{
                return ResponseEntity.ok(res.getValue());
            }
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed to resign");
        }
    }

    // DONE
    @GetMapping("/get-stores/{username}")
    public ResponseEntity<?> getStores(@PathVariable("username") String username) throws Exception{
        try{
            List<StoreDTO> usrStores = service.user_stores(username);
            return ResponseEntity.ok().body(usrStores);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to retrieve stores");
        }
    }

    // DONE
    @GetMapping("/get-permission/{username}")
    public ResponseEntity<?> getPermissions(@PathVariable("username") String username) throws Exception{
        try{
            List<PermissionDTO> pdtos = service.getPermissions(username);
            return ResponseEntity.ok().body(pdtos);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to retrieve permissions");
        }
    }
    
}

package org.market.Web.APIES;

import org.market.PresentationLayer.models.AuthResponse;
import org.market.ServiceLayer.Response;
import org.market.ServiceLayer.ServiceFactory;
import org.market.ServiceLayer.SuspendedException;
import org.market.ServiceLayer.TokenService;
import org.market.Web.DTOS.CartItemDTO;
import org.market.Web.DTOS.PermissionDTO;
import org.market.Web.DTOS.StoreDTO;
import org.market.Web.Requests.*;
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
            return ResponseEntity.status(400).body("Failed to add guest");
        }
    }

    // DONE
    @PostMapping("/guest-exit/{username}")
    public ResponseEntity<?> GuestExit(@PathVariable String username) {
        try{
            String res = service.GuestExit(username);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            return ResponseEntity.status(400).body("Failed to remove guest");
        }
    }

    // DONE
    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody ReqUser user){
        try{
            service.Login(user.getGuest(), user.getUsername(), user.getPassword());
                AuthResponse response = new AuthResponse();
                response.setAccess_token(jwtUtil.generateAccessToken(user.getUsername()));
                response.setRefresh_token(jwtUtil.generateRefreshToken(user.getUsername()));
                response.setManager(service.isSystemManager(user.getUsername()));
                return ResponseEntity.ok(response);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to login");
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
            String response = service.Register(user.getUsername(), user.getPassword(), user.getAge());
            return ResponseEntity.ok(response);
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed to register");
        }
    }

    // DONE
    @GetMapping("/cart/{username}")
    public ResponseEntity<?> getCart(@PathVariable String username) {
        try{
            List<CartItemDTO> response = service.getCart(username);
            return ResponseEntity.ok(response);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed inspect cart.");
        }
    }

    // TODO
    @PostMapping("/buy")
    public ResponseEntity<?> Buy(@RequestBody PaymentReq request) {
        try{
            String response = service.Buy(request.getUsername(), request.getCurrency(),request.getCard(),request.getMonth()
            ,request.getYear(),request.getCcv(),request.getAddress(), request.getCity(), request.getCountry(), request.getZip());
            return ResponseEntity.ok(response);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // DONE
    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody cartOp op){
        try{
            String response = service.addToCart(op.getUsername(), op.getProductId(), op.getStoreId(), op.getQuantity());
            return ResponseEntity.ok(response);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // ALTERNATIVE
    @GetMapping("/inspect-cart/{username}")
    public ResponseEntity<?> inspectCart(@PathVariable String username) {
        try{
            String response = service.inspectCart(username);
            return ResponseEntity.ok(response);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed inspect cart.");
        }
    }

    // DONE
    @PostMapping("/remove-cart-item")
    public ResponseEntity<?> removeCartItem(@RequestBody cartOp op) {
        try{
            String response = service.removeCartItem(op.getUsername(), op.getStoreId(), op.getProductId());
            return ResponseEntity.ok(response);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed to remove cart item.");
        }
    }

    // DONE
    @PostMapping("/edit-permissions")
    public ResponseEntity<?> EditPermissions(@RequestBody PermissionReq per) throws Exception{
        try{
            String res = service.EditPermissions(per.getStoreID(), per.getOwnerUserName(), per.getUsername(), per.getStoreOwner(), per.getStoreManager(), per.getpType());
            return ResponseEntity.ok(res);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed to edit user permissions");
        }
    }

    // DONE
    @PostMapping("/assign-store-manager")
    public ResponseEntity<?> AssignStoreManager(@RequestBody PermissionReq per) throws Exception{
        try {
            String res = service.AssignStoreManager(per.getStoreID(), per.getOwnerUserName(), per.getUsername(), per.getpType());
            return ResponseEntity.ok(res);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed to assign user");
        }
    }

    // DONE
    @PostMapping("/assign-store-owner")
    public ResponseEntity<?> AssignStoreOnwer(@RequestBody PermissionReq per) {
        try{
            String res = service.AssignStoreOnwer(per.getStoreID(), per.getOwnerUserName(), per.getUsername(), per.getpType());
            return ResponseEntity.ok(res);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed to assign user");
        }
    }

    // DONE
    @PostMapping("/unassign-user")
    public ResponseEntity<?> unassignUser(@RequestBody PermissionReq per) {
        try{
            String res = service.unassignUser(per.getStoreID(), per.getOwnerUserName(), per.getUsername());
            return ResponseEntity.ok(res);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e) {
            return ResponseEntity.status(404).body("Failed to unassign user");
        }
    }

    // DONE
    @PostMapping("/resign")
    public ResponseEntity<?> resign(@RequestBody PermissionReq per) throws Exception{
        try{
            String res = service.resign(per.getStoreID(), per.getOwnerUserName());
            return ResponseEntity.ok(res);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
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
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
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
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to retrieve permissions");
        }
    }

    // DONE
    @PostMapping("/suspend-user-indefinitely")
    public ResponseEntity<?> suspendUserIndefinitely(@RequestBody SuspendReq req) {
        try{
            service.suspendUserIndefinitely(req.getManager(),req.getUsername());
            return ResponseEntity.ok("Successfully suspended user");
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to suspend user");
        }
    }

    // DONE
    @PostMapping("/suspend-user-temporarily")
    public ResponseEntity<?> suspendUserTemporarily(@RequestBody SuspendReq req){
        try{
            service.suspendUserTemporarily(req.getManager(),req.getUsername(), req.getDuration());
            return ResponseEntity.ok("Successfully suspended user");
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to suspend user");
        }
    }

    // DONE
    @PostMapping("/resume-user-indefinitely")
    public ResponseEntity<?> resumeUserIndefinitely(@RequestBody SuspendReq req) {
        try{
            service.resumeUserIndefinitely(req.getManager(),req.getUsername());
            return ResponseEntity.ok("Successfully resumed suspended user");
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to resume suspended user");
        }
    }

    // DONE
    @PostMapping("/resume-user-temporarily")
    public ResponseEntity<?> resumeUser(@RequestBody SuspendReq req) {
        try{
            service.resumeUser(req.getManager(),req.getUsername(),req.getDuration());
            return ResponseEntity.ok("Successfully resumed suspended user");
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to resume suspended user");
        }
    }

    // DONE
    @GetMapping("/suspended-users/{manager}")
    public ResponseEntity<?> viewSuspended(@PathVariable("manager") String manager) {
        try{
            String res = service.viewSuspended(manager);
            return ResponseEntity.ok(res);

        }
        catch(Exception e){
            return ResponseEntity.status(404).body("Failed to retrieve suspended users");
        }
    }


    // DONE
    @GetMapping("/purchase-history/{username}")
    public ResponseEntity<?> getPurchaseHistory(@PathVariable("username") String username) {
        try{
            List<String> res = service.getPurchaseHistory(username);
            return ResponseEntity.ok(res);

        }
        catch(Exception e){
            return ResponseEntity.status(404).body("Failed to retrieve user purchase history");
        }
    }

    // DONE
    @PostMapping("/remove-purchase/user={username}&purchase={purchase_id}")
    public ResponseEntity<?> removePurchaseStore(@PathVariable("username") String username,
                                                 @PathVariable("purchase_id") Integer purchase_id) {
        try{
            String res = service.removePurchaseUser(username, purchase_id);
            return ResponseEntity.ok(res);

        }
        catch(Exception e){
            return ResponseEntity.status(404).body("Failed to remove user purchase history");
        }
    }
}

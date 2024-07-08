package org.market.Web.APIES;

import java.util.List;

import org.market.PresentationLayer.models.AuthResponse;
import org.market.ServiceLayer.Response;
import org.market.ServiceLayer.ServiceFactory;
import org.market.ServiceLayer.TokenService;
import org.market.Web.DTOS.StoreDTO;
import org.market.Web.DTOS.PermissionDTO;
import org.market.Web.Requests.*;
import org.market.Web.Requests.cartOp;
import org.market.Web.SocketCommunication.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {
    private ServiceFactory service;
    private TokenService jwtUtil;

    private SocketHandler socketHandler;

    @Autowired
    public UserController(ServiceFactory service, TokenService jwtUtil, SocketHandler socketHandler){
        this.service = service;
        this.jwtUtil = jwtUtil;
        this.socketHandler = socketHandler;
    }

    @PostMapping("/send-message")
    public ResponseEntity<Boolean> TestSendMessage(@RequestBody ReqUser user){
        try{ socketHandler.sendMessage("Waleed", "Attention!! this is a notification.");}catch (Exception e){}
        return ResponseEntity.ok(true);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> RefreshToken(@RequestBody String refresh_token){
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
    }


    @PostMapping("/enter-as-guest/{age}")
    public ResponseEntity<?> enterAsGuest(@PathVariable Double age){
        String res = service.EnterAsGuest(age);
        return ResponseEntity.ok(Response.successRes(res));
    }


    @PostMapping("/guest-exit/{username}")
    public ResponseEntity<?> GuestExit(@PathVariable String username) {
        String res = service.GuestExit(username);
        return ResponseEntity.ok(Response.successRes(res));
    }

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody ReqUser user){
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

    }

    @PostMapping("/logout/{username}")
    public ResponseEntity<?> Logout(@PathVariable String username){
        String res = service.Logout(username);
        return ResponseEntity.ok(Response.successRes(res));
    }

    @PostMapping("/register")
    public ResponseEntity<?> Register(@RequestBody ReqUser user){
         Response<String> response = service.Register(user.getUsername(), user.getPassword(), user.getAge());
         if (response.isError()){
             return ResponseEntity.status(400).body(response.getErrorMessage());
         }
         else{
             return ResponseEntity.ok(response);
         }
    }

    @PostMapping("/buy/{username}")
    public Response<String> Buy(@PathVariable String username) {
        return Response.failRes("Not implemented");
       // return service.Buy(username);
    }

    @PostMapping("/add-to-cart")
    public Response<String> addToCart(@RequestBody cartOp op){
        return service.addToCart(op.getUsername(), op.getProdID(), op.getStoreID(), op.getQuant());
    }

    @GetMapping("/inspect-cart/{username}")
    public Response<String> inspectCart(@PathVariable String username) {
        return service.inspectCart(username);
    }

    @PostMapping("/remove-cart-item")
    public Response<String> removeCartItem(@RequestBody cartOp op) {
        return service.removeCartItem(op.getUsername(), op.getStoreID(), op.getProdID());
    }

    @PostMapping("/edit-permissions")
    public Response<String> EditPermissions(@RequestBody PermissionReq per) throws Exception{
        return service.EditPermissions(per.getStoreID(), per.getOwnerUserName(), per.getUsername(), per.isStoreOwner(), per.isStoreManager(), per.getPType());
    }

    @PostMapping("/assign-store-manager")
    public Response<String> AssignStoreManager(@RequestBody PermissionReq per) throws Exception{
        return service.AssignStoreManager(per.getStoreID(), per.getOwnerUserName(), per.getUsername(), per.getPType());
    }

    @PostMapping("/assign-store-owner")
    public Response<String> AssignStoreOnwer(@RequestBody PermissionReq per) throws Exception{
        return service.AssignStoreOnwer(per.getStoreID(), per.getOwnerUserName(), per.getUsername(), per.getPType());
    }

    @PostMapping("/unassign-user")
    public Response<String> unassignUser(@RequestBody PermissionReq per) throws Exception{
        return service.unassignUser(per.getStoreID(), per.getOwnerUserName(), per.getUsername());
    }
    @GetMapping("/get-stores/{username}")
    public ResponseEntity<?> getStores(@PathVariable String username) throws Exception{
        List<StoreDTO> usrStores = service.user_stores(username);
        return ResponseEntity.ok().body(usrStores);
    }

    @GetMapping("/get-permission/{username}")
    public ResponseEntity<?> getPermissions(@PathVariable String username) throws Exception{
        List<PermissionDTO> pdtos = service.getPermissions(username);
        return ResponseEntity.ok().body(pdtos);
    }
}

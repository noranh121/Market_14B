package Web.APIES;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ServiceLayer.Response;
import ServiceLayer.ServiceFactory;
import Web.Requests.*;
import Web.Requests.cartOp;

@RestController
@RequestMapping("api/users")
public class UserController {
    private ServiceFactory service;

    @Autowired
    public UserController(ServiceFactory service){
        this.service = service;
    }



    @PostMapping("/enter-as-guest/{age}")
    public Response<String> enterAsGuest(@PathVariable Double age){
        String res = service.EnterAsGuest(age);
        return Response.successRes(res);
    }


    @PostMapping("/guest-exit/{username}")
    public Response<String> GuestExit(@PathVariable String username) {
        String res = service.GuestExit(username);
        return Response.successRes(res);
    }

    @PostMapping("/login")
    public Response<String> Login(@RequestBody ReqUser user){
       String res =  service.Login(user.getGuest(), user.getUsername(), user.getPass());
       return Response.successRes(res);
    }

    @PostMapping("/logout/{username}")
    public Response<String> Logout(@PathVariable String username){
        String res = service.Logout(username);
        return Response.successRes(res);
    }

    @PostMapping("/register")
    public Response<String> Register(@RequestBody ReqUser user){
         return service.Register(user.getUsername(), user.getPass(), user.getAge());
    }

    @PostMapping("/buy/{username}")
    public Response<String> Buy(@PathVariable String username) {
        return service.Buy(username);
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
    
}

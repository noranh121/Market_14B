package Web.APIES;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ServiceLayer.Response;
import ServiceLayer.ServiceFactory;
import Web.Requests.ReqStore;


@RestController
@RequestMapping("api/stores")
public class StoreController {

    private ServiceFactory service;
    

    @Autowired
    public StoreController(ServiceFactory service){
        this.service = service;
    }


    @PostMapping("/add-store/username={username}&desc={desc}")
    public Response<String> initStore(
        @PathVariable("username") String userName,
        @PathVariable("desc") String Description) throws Exception{
        return service.initStore(userName, Description);
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestBody ReqStore rstr) throws Exception{
        return service.addProduct(rstr.getProdID(), rstr.getStoreID(), rstr.getPrice(), rstr.getQuantity(), rstr.getUsername(), rstr.getWeight());
    }

    @DeleteMapping("/remove-product")
    public Response<String> removeProduct(@RequestBody ReqStore rstr){
        return service.removeProduct(rstr.getProdID(), rstr.getStoreID(), rstr.getUsername());
    }

    @PutMapping("/edit-product-price")
    public Response<String> EditProducPrice(@RequestBody ReqStore rstr) throws Exception{
        return service.EditProducPrice(rstr.getProdID(), rstr.getStoreID(), rstr.getPrice(),rstr.getUsername());
    }

    @PutMapping("/edit-product-quantity")
    public Response<String> EditProductQuantity(@RequestBody ReqStore rstr) throws Exception{
        return service.EditProductQuantity(rstr.getProdID(), rstr.getStoreID(),rstr.getQuantity(), rstr.getUsername());
    }


    @PostMapping("/close-store")
    public Response<String> closeStore(@RequestBody ReqStore rstr) {
        return service.closeStore(rstr.getStoreID(), rstr.getUsername());
    }

    @PostMapping("/open-store")
    public Response<String> openStore(@RequestBody ReqStore rstr) {
        return service.openStore(rstr.getStoreID(), rstr.getUsername());
    }

    @GetMapping("/get-store-info")
    public Response<String> getInfo(@RequestBody ReqStore rstr) {
        return service.getInfo(rstr.getStoreID(), rstr.getUsername());
    }
    
}
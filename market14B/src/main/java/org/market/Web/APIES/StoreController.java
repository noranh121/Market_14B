package org.market.Web.APIES;

import org.market.ServiceLayer.Response;
import org.market.ServiceLayer.ServiceFactory;
import org.market.ServiceLayer.TokenService;
import org.market.Web.DTOS.StoreDTO;
import org.market.Web.Requests.ReqStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("api/stores")
public class StoreController {

    private ServiceFactory service;
    private TokenService jwtUtil;


    @Autowired
    public StoreController(ServiceFactory service, TokenService jwtUtil){
        this.service = service;
        this.jwtUtil = jwtUtil;
    }



    @GetMapping("/all")
    public ResponseEntity<?> getInfo() {
        ArrayList<StoreDTO> stores = new ArrayList<>();
        StoreDTO store1 = new StoreDTO();
        store1.setId(1);
        store1.setName("store1");
        store1.setDescription("desc1");
        stores.add(store1);

        StoreDTO store2 = new StoreDTO();
        store2.setId(2);
        store2.setName("store2");
        store2.setDescription("desc2");
        stores.add(store2);

        StoreDTO store3 = new StoreDTO();
        store3.setId(3);
        store3.setName("store3");
        store3.setDescription("desc3");
        stores.add(store3);

        return ResponseEntity.ok().body(stores);
    }

    @PostMapping("/add-store/username={username}&desc={desc}")
    public ResponseEntity<?> initStore(@RequestHeader("Authorization") String token,
                                       @PathVariable("username") String userName,
                                       @PathVariable("desc") String Description) throws Exception{
        String tokenValue = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(tokenValue);
        if (username != null && jwtUtil.validateToken(tokenValue,username)) {
            Response<String> res = service.initStore(userName, Description);
            if(res.isError()){
                return ResponseEntity.status(400).body(res.getErrorMessage());
            }
            else{
                return ResponseEntity.ok(res);
            }
        }
        return ResponseEntity.status(401).build();
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

package org.market.Web.APIES;

import org.market.ServiceLayer.Response;
import org.market.ServiceLayer.ServiceFactory;
import org.market.ServiceLayer.TokenService;
import org.market.Web.DTOS.ProductDTO;
import org.market.Web.DTOS.StoreDTO;
import org.market.Web.Requests.AddProductReq;
import org.market.Web.Requests.AddStoreReq;
import org.market.Web.Requests.ReqStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


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
    public ResponseEntity<?> getAllStores(){
        try{
            ArrayList<StoreDTO> stores = service.getAllStores();
            return ResponseEntity.ok().body(stores);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to get all stores.");
        }
    }

    @GetMapping("/products/all")
    public ResponseEntity<?> getAllProducts(){
        try{
            List<ProductDTO> prods = service.getAllProducts();
            return ResponseEntity.ok().body(prods);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to get all products.");
        }
    }
    @GetMapping("/products/{store_id}")
    public ResponseEntity<?> getStoreProducts(@PathVariable("store_id") int store_id){
        try{
            List<ProductDTO> prods = service.getStoreProducts(store_id);
            return ResponseEntity.ok().body(prods);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to get store products.");
        }
    }

    @GetMapping("/product/{product_id}")
    public ResponseEntity<?> getProductInfo(@PathVariable("product_id") int product_id){
        try{
            ProductDTO pdto = service.getProductInfo(product_id);
            return ResponseEntity.ok().body(pdto);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to get product info.");
        }
    }

    @GetMapping("/store/{store_id}")
    public ResponseEntity<?> getStoreInfo(@PathVariable("store_id") int store_id){
        try{
            StoreDTO sdto =  service.getStore(store_id);
            return ResponseEntity.ok().body(sdto);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to get store info.");
        }
    }

    @GetMapping("/mystores/{username}")
    public ResponseEntity<?> getUserStores(@PathVariable("username") String username){
        try{
            List<StoreDTO> stores =  service.user_stores(username);
            return ResponseEntity.ok().body(stores);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to get user stores.");
        }
    }

    @PostMapping("/add-store")
    public ResponseEntity<?> initStore(@RequestHeader("Authorization") String token,
                                       @RequestBody AddStoreReq request) throws Exception{
        try{
            String tokenValue = token.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(tokenValue);
            if (username != null && jwtUtil.validateToken(tokenValue,username)) {
                Response<String> res = service.initStore(request.getUsername(), request.getName(), request.getDescription());
                if(res.isError()){
                    return ResponseEntity.status(400).body(res.getErrorMessage());
                }
                else{
                    return ResponseEntity.ok(res.getValue());
                }
            }
            return ResponseEntity.status(401).build();
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to get add store.");
        }
    }

    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(@RequestHeader("Authorization") String token, @RequestBody AddProductReq rstr) throws Exception{
        try{
            String tokenValue = token.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(tokenValue);
            if (username != null && jwtUtil.validateToken(tokenValue,username)) {
                Response<Integer> response1 = service.initProduct(rstr.getUsername(), rstr.getName(),-1,rstr.getDescription(),rstr.getBrand(),rstr.getWeight());
                if(!response1.isError()){
                    String response2 = service.addProduct(response1.getValue(), rstr.getStore_id(), rstr.getPrice(), rstr.getInventory(), rstr.getUsername(), rstr.getWeight());
                    if(!response2.equals("Product added to store Successfully")) {
                        return ResponseEntity.badRequest().body(response2);
                    }else{
                        return ResponseEntity.ok(response2);
                    }
                }
                else {
                    return ResponseEntity.badRequest().body(response1.getErrorMessage());
                }
            }
            return ResponseEntity.status(401).build();
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to retrieve add product.");
        }
    }

    @DeleteMapping("/remove-product")
    public ResponseEntity<?> removeProduct(@RequestBody ReqStore rstr){
        try{
            Response<String> response = service.removeProduct(rstr.getProdID(), rstr.getStoreID(), rstr.getUsername());
            if(response.isError()){
                return ResponseEntity.badRequest().body(response.getErrorMessage());
            }
            else{
                return ResponseEntity.ok(response.getValue());
            }
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to remove product.");
        }
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

package org.market.Web.APIES;

import org.market.ServiceLayer.ServiceFactory;
import org.market.ServiceLayer.SuspendedException;
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

    // DONE
    @GetMapping("/all")
    public ResponseEntity<?> getAllStores(){
        try{
            ArrayList<StoreDTO> stores = service.getAllStores();
            return ResponseEntity.ok().body(stores);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to get all stores.");
        }
    }

    // DONE
    @GetMapping("/products/all")
    public ResponseEntity<?> getAllProducts(){
        try{
            List<ProductDTO> prods = service.getAllProducts();
            return ResponseEntity.ok().body(prods);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to get all products.");
        }
    }

    // DONE
    @GetMapping("/products/{store_id}")
    public ResponseEntity<?> getStoreProducts(@PathVariable("store_id") int store_id){
        try{
            List<ProductDTO> prods = service.getStoreProducts(store_id);
            return ResponseEntity.ok().body(prods);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to get store products.");
        }
    }

    // DONE
    @GetMapping("/product/{product_id}")
    public ResponseEntity<?> getProductInfo(@PathVariable("product_id") int product_id){
        try{
            ProductDTO pdto = service.getProductInfo(product_id);
            return ResponseEntity.ok().body(pdto);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to get product info.");
        }
    }

    // DONE
    @GetMapping("/store/{store_id}")
    public ResponseEntity<?> getStoreInfo(@PathVariable("store_id") int store_id){
        try{
            StoreDTO sdto =  service.getStore(store_id);
            return ResponseEntity.ok().body(sdto);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to get store info.");
        }
    }

    // DONE
    @GetMapping("/mystores/{username}")
    public ResponseEntity<?> getUserStores(@PathVariable("username") String username){
        try{
            List<StoreDTO> stores =  service.user_stores(username);
            return ResponseEntity.ok().body(stores);
        }catch(Exception e){
            return ResponseEntity.status(404).body("Failed to get user stores.");
        }
    }

    // DONE
    @PostMapping("/add-store")
    public ResponseEntity<?> initStore(@RequestHeader("Authorization") String token,
                                       @RequestBody AddStoreReq request) throws Exception{
        try{
            String tokenValue = token.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(tokenValue);
            if (username != null && jwtUtil.validateToken(tokenValue,username)) {
                String res = service.initStore(request.getUsername(), request.getName(), request.getDescription());
                return ResponseEntity.ok(res);
            }
            return ResponseEntity.status(401).build();
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(400).body("Failed to add store.");
        }
    }

    // DONE
    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(@RequestHeader("Authorization") String token, @RequestBody AddProductReq rstr) throws Exception{
        try{
            String tokenValue = token.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(tokenValue);
            if (username != null && jwtUtil.validateToken(tokenValue,username)) {
                Integer category = service.getCategory(rstr.getCategory());
                Integer response1 = service.initProduct(rstr.getUsername(), rstr.getName(),category,rstr.getDescription(),rstr.getBrand(),rstr.getWeight());
                String response2 = service.addProduct(response1, rstr.getStore_id(), rstr.getPrice(), rstr.getInventory(), rstr.getUsername(), rstr.getWeight());
                return ResponseEntity.ok(response2);
            }
            return ResponseEntity.status(401).build();
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(400).body("Failed to add product.");
        }
    }

    // DONE
    @DeleteMapping("/remove-product")
    public ResponseEntity<?> removeProduct(@RequestBody ReqStore rstr) throws Exception{
        try {
            String response = service.removeProduct(rstr.getProductId(), rstr.getStoreId(), rstr.getUsername());
            return ResponseEntity.ok(response);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(400).body("Failed to remove product.");
        }
    }

    // DONE
    @PutMapping("/edit-product-price")
    public ResponseEntity<?> EditProducPrice(@RequestBody ReqStore rstr) throws Exception{
        try {
            String response = service.EditProducPrice(rstr.getProductId(), rstr.getStoreId(), rstr.getPrice(),rstr.getUsername());
            return ResponseEntity.ok(response);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(400).body("Failed to edit product price.");
        }
    }

    // DONE
    @PutMapping("/edit-product-quantity")
    public ResponseEntity<?> EditProductQuantity(@RequestBody ReqStore rstr) throws Exception{
        try {
            String response = service.EditProductQuantity(rstr.getProductId(), rstr.getStoreId(),rstr.getQuantity(), rstr.getUsername());
            return ResponseEntity.ok(response);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(400).body("Failed to edit product inventory.");
        }
    }

    // DONE
    @PostMapping("/close-store")
    public ResponseEntity<?> closeStore(@RequestBody ReqStore rstr) {
        try {
            String response = service.closeStore(rstr.getStoreId(), rstr.getUsername());
            return ResponseEntity.ok(response);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(400).body("Failed to close store.");
        }
    }

    // DONE
    @PostMapping("/open-store")
    public ResponseEntity<String> openStore(@RequestBody ReqStore rstr) {
        try {
            String response = service.openStore(rstr.getStoreId(), rstr.getUsername());
            return ResponseEntity.ok(response);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(400).body("Failed to open store.");
        }
    }

    // ALTERNATIVE
    @GetMapping("/get-store-info")
    public ResponseEntity<?> getInfo(@RequestBody ReqStore rstr) {
        try {
            String response = service.getInfo(rstr.getStoreId(), rstr.getUsername());
            return ResponseEntity.ok(response);
        }catch(SuspendedException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(400).body("Failed to get store info.");
        }
    }

    // DONE
    @GetMapping("/purchase-history/{store_id}")
    public ResponseEntity<?> getPurchaseHistory(@PathVariable("store_id") Integer store_id) {
        try{
            List<String> res = service.getStorePurchaseHistory(store_id);
            return ResponseEntity.ok(res);

        }
        catch(Exception e){
            return ResponseEntity.status(404).body("Failed to retrieve store purchase history");
        }
    }

    // DONE
    @PostMapping("/remove-purchase/store={store_id}&purchase={purchase_id}")
    public ResponseEntity<?> removePurchaseStore(@PathVariable("store_id") Integer store_id,
                                                @PathVariable("purchase_id") Integer purchase_id) {
        try{
            String res = service.removePurchaseStore(store_id, purchase_id);
            return ResponseEntity.ok(res);

        }
        catch(Exception e){
            return ResponseEntity.status(404).body("Failed to remove store purchase history");
        }
    }

}

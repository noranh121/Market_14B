package org.market.Web.APIES;

import org.market.ServiceLayer.Response;
import org.market.ServiceLayer.ServiceFactory;
import org.market.ServiceLayer.TokenService;
import org.market.Web.DTOS.ProductDTO;
import org.market.Web.DTOS.StoreDTO;
import org.market.Web.Requests.AddProductReq;
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
        ArrayList<StoreDTO> stores = service.getAllStores();
        return ResponseEntity.ok().body(stores);
    }

    @GetMapping("/products/all")
    public ResponseEntity<?> getAllProducts(){
        List<ProductDTO> prods = service.getAllProducts();
        return ResponseEntity.ok().body(prods);
    }
    @GetMapping("/products/{store_id}")
    public ResponseEntity<?> getStoreProducts(@PathVariable("store_id") int store_id){
        List<ProductDTO> prods = service.getStoreProducts(store_id);
        return ResponseEntity.ok().body(prods);
    }
    @GetMapping("/product/{product_id}")
    public ResponseEntity<?> getProductInfo(@PathVariable("product_id") int product_id){
        try{
            ProductDTO pdto = service.getProductInfo(product_id);
            return ResponseEntity.ok().body(pdto);
        }catch(Exception e){
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/store/{store_id}")
    public ResponseEntity<?> getStoreInfo(@PathVariable("store_id") int store_id){
        StoreDTO sdto =  service.getStore(store_id);
        return ResponseEntity.ok().body(sdto);
    }

//    @GetMapping("/all")
//    public ResponseEntity<?> getAllStores() {
//        ArrayList<StoreDTO> stores = new ArrayList<>();
//        StoreDTO store1 = new StoreDTO();
//        store1.setId(1);
//        store1.setName("store1");
//        store1.setDescription("desc1");
//        stores.add(store1);
//
//        StoreDTO store2 = new StoreDTO();
//        store2.setId(2);
//        store2.setName("store2");
//        store2.setDescription("desc2");
//        stores.add(store2);
//
//        StoreDTO store3 = new StoreDTO();
//        store3.setId(3);
//        store3.setName("store3");
//        store3.setDescription("desc3");
//        stores.add(store3);
//
//        return ResponseEntity.ok().body(stores);
//    }
//
//    @GetMapping("/products/all")
//    public ResponseEntity<?> getAllProducts() {
//        ArrayList<ProductDTO> products = new ArrayList<>();
//        ProductDTO product1 = new ProductDTO();
//        product1.setId(1);
//        product1.setName("product1");
//        product1.setDescription("desc1");
//        product1.setPrice(10.0);
//        products.add(product1);
//
//        ProductDTO product2 = new ProductDTO();
//        product2.setId(2);
//        product2.setName("product2");
//        product2.setDescription("desc2");
//        product2.setPrice(10.0);
//        products.add(product2);
//
//        ProductDTO product3 = new ProductDTO();
//        product3.setId(3);
//        product3.setName("product3");
//        product3.setDescription("desc3");
//        product3.setPrice(10.0);
//        products.add(product3);
//
//        return ResponseEntity.ok().body(products);
//    }
//
//    @GetMapping("/products/{store_id}")
//    public ResponseEntity<?> getStoreProducts(@PathVariable("store_id") int store_id) {
//        ArrayList<ProductDTO> products = new ArrayList<>();
//        ProductDTO product1 = new ProductDTO();
//        product1.setId(1);
//        product1.setName("product1");
//        product1.setDescription("desc1");
//        product1.setPrice(10.0);
//        products.add(product1);
//
//        ProductDTO product2 = new ProductDTO();
//        product2.setId(2);
//        product2.setName("product2");
//        product2.setDescription("desc2");
//        product2.setPrice(10.0);
//        products.add(product2);
//
//        ProductDTO product3 = new ProductDTO();
//        product3.setId(3);
//        product3.setName("product3");
//        product3.setDescription("desc3");
//        product3.setPrice(10.0);
//        products.add(product3);
//
//        return ResponseEntity.ok().body(products);
//    }
//
//    @GetMapping("/product/{product_id}")
//    public ResponseEntity<?> getProductInfo(@PathVariable("product_id") int product_id) {
//        ProductDTO product = new ProductDTO();
//        product.setId(1);
//        product.setName("Awesome Product");
//        product.setDescription("This product is very Awesome.");
//        product.setPrice(70.0);
//        return ResponseEntity.ok().body(product);
//    }
//
//    @GetMapping("/store/{store_id}")
//    public ResponseEntity<?> getStoreInfo(@PathVariable("store_id") int store_id) {
//        StoreDTO store = new StoreDTO();
//        store.setId(1);
//        store.setName("Awesome Store");
//        store.setDescription("This store is very Awesome.");
//        return ResponseEntity.ok().body(store);
//    }

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
    public ResponseEntity<?> addProduct(@RequestHeader("Authorization") String token, @RequestBody AddProductReq rstr) throws Exception{
        String tokenValue = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(tokenValue);
        if (username != null && jwtUtil.validateToken(tokenValue,username)) {
            Response<Integer> response1 = service.initProduct(rstr.getUsername(), rstr.getName(),-1,rstr.getDescription(),"brand",rstr.getWeight());
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
    }

    @DeleteMapping("/remove-product")
    public ResponseEntity<?> removeProduct(@RequestBody ReqStore rstr){
        Response<String> response = service.removeProduct(rstr.getProdID(), rstr.getStoreID(), rstr.getUsername());
        if(response.isError()){
            return ResponseEntity.badRequest().body(response.getErrorMessage());
        }
        else{
            return ResponseEntity.ok(response.getValue());
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

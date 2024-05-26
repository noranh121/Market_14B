package DomainLayer.backend.UserPackage;

import DomainLayer.backend.Permissions;
import DomainLayer.backend.StorePackage.StoreController;

import java.util.logging.Logger;

public class RegisteredUser extends User{

    private static final Logger LOGGER = Logger.getLogger(RegisteredUser.class.getName());
    private StoreController storeController = StoreController.getInstance();
    private String password;


    private int currentStore; //storeId
    public RegisteredUser(String username, String password) {
        super(username);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

//TODO
//    public String openStore() {
//        // new store
//        return "";
//    }
//    public String AddProduct(int productId,int storeId,int price,String productName,int quantity) {
//
//    }
//    public String RemoveProduct(int productId,int storeId) {
//
//    }
//    public String EditProductName(int productId,int storeId,String newName) {
//
//    }
//    public String EditProductPrice(int productId,int storeId,int newPrice) {
//
//    }
//    public String EditProductQuantity(int productId,int storeId,int newQuantity) {
//
//    }
    public String AssignStoreOwner(int storeId,String username,Boolean[] pType) throws Exception {
        return Permissions.getInstance().addPermission(storeId,this.getUsername(),username,true,false,pType);

    }
    public String AssignStoreManager(int storeId,String username,Boolean[] pType) throws Exception {
        return Permissions.getInstance().addPermission(storeId,this.getUsername(),username,false,true,pType);
    }
    public String EditPermissions(int storeID, String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) throws Exception {
        return Permissions.getInstance().editPermission(storeID,this.getUsername(),userName,storeOwner,storeManager,pType);
    }
//    public String CloseStore(int storeId) {
//
//    }
//    public String getInfo(int storeId) {
//
//    }

}

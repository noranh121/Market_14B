package DomainLayer.backend.UserPackage;

import DomainLayer.backend.Permissions;
import DomainLayer.backend.StorePackage.StoreController;

import java.util.logging.Logger;

public class RegisteredUser extends User{

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

    public String AssignStoreOwner(int storeId,String username,Boolean[] pType) throws Exception {
        return Permissions.getInstance().addPermission(storeId,this.getUsername(),username,true,false,pType);

    }
    public String AssignStoreManager(int storeId,String username,Boolean[] pType) throws Exception {
        return Permissions.getInstance().addPermission(storeId,this.getUsername(),username,false,true,pType);
    }
    public String EditPermissions(int storeID, String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) throws Exception {
        return Permissions.getInstance().editPermission(storeID,this.getUsername(),userName,storeOwner,storeManager,pType);
    }

}

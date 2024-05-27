package DomainLayer.backend;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Permissions {
    private static final Logger LOGGER = Logger.getLogger(Permissions.class.getName());

    private Map<Integer, Tree> storeOwners = new HashMap<>();

    private static Permissions instance = null;

    private Permissions() {
    }

    public static synchronized Permissions getInstance() {
        if (instance == null)
            instance = new Permissions();
        return instance;
    }

    public String initStore(int storeID,String userName) throws Exception {
        if(!storeOwners.containsKey(storeID)){
            Boolean[] pType=new Boolean[3];
            pType[0]=true;pType[1]=true;pType[2]=true;
            Permission fisrtOwner = new Permission(userName,true,false,pType);
            Tree storeTree=new Tree(fisrtOwner);
            storeOwners.put(storeID,storeTree);
            LOGGER.info("store added successfully");
            return "store added successfully";
        }
        else{
            LOGGER.severe(storeID + " already exists");
            throw new Exception(storeID + " already exists");
        }
    }

    public Permission getPermission(int storeID,String userName) throws Exception {
        if(storeOwners.containsKey(storeID)){
            Node permissionNode= storeOwners.get(storeID).findNode(userName);
            if(permissionNode != null){
                LOGGER.info("Permission found");
                return permissionNode.getData();
            } else{
                LOGGER.severe(userName + " does not exist");
                throw new Exception(userName + " does not exist");
            }
        } else{
            LOGGER.severe(storeID + " does not exist");
            throw new Exception(storeID + " does not exist");
        }
    }

    public String addPermission(int storeID, String ownerUserName, String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) throws Exception {
        Permission permission = new Permission(userName, storeOwner, storeManager, pType);
        Node permissionNode = new Node(permission);
        if (storeOwners.containsKey(storeID)) {
            if (storeOwners.get(storeID).findNode(ownerUserName).getData().getStoreOwner()) {
                if (storeOwners.get(storeID).findNode(userName) == null) {
                    storeOwners.get(storeID).findNode(ownerUserName).addChild(permissionNode);
                    LOGGER.info("Permission added to store");
                    return "Permission added to store";

                } else {
                    LOGGER.severe(userName + " already exists");
                    throw new Exception(userName + " already exists");
                }
            } else {
                LOGGER.severe(ownerUserName + " not owner");
                throw new Exception(ownerUserName + " not owner");
            }
        } else {
            LOGGER.severe(storeID + " does not exist");
            throw new Exception(storeID + " does not exist");
        }
    }

    public String editPermission(int storeID, String ownerUserName, String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) throws Exception {
        Permission permission = new Permission(userName, storeOwner, storeManager, pType);
        if (storeOwners.containsKey(storeID)) {
            if (storeOwners.get(storeID).findNode(ownerUserName).getData().getStoreOwner()) {
                if (storeOwners.get(storeID).findNode(ownerUserName).isChild(userName)) {
                    storeOwners.get(storeID).findNode(userName).edit(permission);
                    LOGGER.info("Permission added to store");
                    return "Permission added to store";
                } else {
                    LOGGER.severe(userName + " is not employed by " + ownerUserName);
                    throw new Exception(userName + " is not employed by " + ownerUserName);
                }
            } else {
                LOGGER.severe(ownerUserName + " does not exist");
                throw new Exception(ownerUserName + " does not exist");
            }
        } else {
            LOGGER.severe(storeID + " does not exist");
            throw new Exception(storeID + " does not exist");
        }
    }

    public String deletePermission(int storeID, String ownerUserName, String userName) throws Exception {
        if (storeOwners.containsKey(storeID)) {
            if (storeOwners.get(storeID).findNode(ownerUserName).getData().getStoreOwner()) {
                if (storeOwners.get(storeID).findNode(userName) != null) {
                    if (storeOwners.get(storeID).findNode(ownerUserName).isChild(userName) || ownerUserName.equals(userName)) {
                        storeOwners.get(storeID).deleteNode(userName);
                        LOGGER.info("Permission deleted from store");
                        return "Permission deleted from store";
                    } else {
                        LOGGER.severe(userName + " is not employed by " + ownerUserName);
                        throw new Exception(userName + " is not employed by " + ownerUserName);
                    }
                } else {
                    LOGGER.severe(userName + " already exists");
                    throw new Exception(userName + " already exists");
                }
            } else {
                LOGGER.severe(ownerUserName + " does not exist");
                throw new Exception(ownerUserName + " does not exist");
            }
        } else {
            LOGGER.severe(storeID + " does not exist");
            throw new Exception(storeID + " does not exist");
        }
    }

}

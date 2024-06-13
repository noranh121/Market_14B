package DomainLayer.backend;

import DomainLayer.backend.StorePackage.StoreController;
import DomainLayer.backend.UserPackage.UserController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Permissions {
    private Map<Integer, Tree> storeOwners = new HashMap<>();
    private HashMap<String, suspensionInfo> suspendedUsers = new HashMap<>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static Permissions instance = null;

    private Permissions() {
    }

    public static synchronized Permissions getInstance() {
        if (instance == null)
            instance = new Permissions();
        return instance;
    }

    public String initStore(int storeID, String userName) throws Exception {
        if (!storeOwners.containsKey(storeID)) {
            Boolean[] pType = new Boolean[3];
            pType[0] = true;
            pType[1] = true;
            pType[2] = true;
            Permission fisrtOwner = new Permission(userName, true, false, pType);
            Tree storeTree = new Tree(fisrtOwner);
            storeOwners.put(storeID, storeTree);
            UserController.LOGGER.info("store added successfully");
            return "store added successfully";
        } else {
            UserController.LOGGER.severe(storeID + " already exists");
            throw new Exception(storeID + " already exists");
        }
    }

    public Permission getPermission(int storeID, String userName) throws Exception {
        if (storeOwners.containsKey(storeID)) {
            Node permissionNode = storeOwners.get(storeID).findNode(userName);
            if (permissionNode != null) {
                UserController.LOGGER.info("Permission found");
                return permissionNode.getData();
            } else {
                UserController.LOGGER.severe(userName + " does not exist");
                throw new Exception(userName + " does not exist");
            }
        } else {
            UserController.LOGGER.severe(storeID + " does not exist");
            throw new Exception(storeID + " does not exist");
        }
    }

    public String addPermission(int storeID, String ownerUserName, String userName, Boolean storeOwner,
            Boolean storeManager, Boolean[] pType) throws Exception {
        Permission permission = new Permission(userName, storeOwner, storeManager, pType);
        Node permissionNode = new Node(permission);
        if (storeOwners.containsKey(storeID)) {
            if (storeOwners.get(storeID).findNode(ownerUserName).getData().getStoreOwner()) {
                if (storeOwners.get(storeID).findNode(userName) == null) {
                    storeOwners.get(storeID).findNode(ownerUserName).addChild(permissionNode);
                    UserController.LOGGER.info("Permission added to store");
                    return "Permission added to store";

                } else {
                    UserController.LOGGER.severe(userName + " already exists");
                    throw new Exception(userName + " already exists");
                }
            } else {
                UserController.LOGGER.severe(ownerUserName + " not owner");
                throw new Exception(ownerUserName + " not owner");
            }
        } else {
            UserController.LOGGER.severe(storeID + " does not exist");
            throw new Exception(storeID + " does not exist");
        }
    }

    public String editPermission(int storeID, String ownerUserName, String userName, Boolean storeOwner,
            Boolean storeManager, Boolean[] pType) throws Exception {
        Permission permission = new Permission(userName, storeOwner, storeManager, pType);
        if (storeOwners.containsKey(storeID)) {
            if (storeOwners.get(storeID).findNode(ownerUserName).getData().getStoreOwner()) {
                if (storeOwners.get(storeID).findNode(ownerUserName).isChild(userName)) {
                    storeOwners.get(storeID).findNode(userName).edit(permission);
                    UserController.LOGGER.info("Permission added to store");
                    return "Permission added to store";
                } else {
                    UserController.LOGGER.severe(userName + " is not employed by " + ownerUserName);
                    throw new Exception(userName + " is not employed by " + ownerUserName);
                }
            } else {
                UserController.LOGGER.severe(ownerUserName + " does not exist");
                throw new Exception(ownerUserName + " does not exist");
            }
        } else {
            UserController.LOGGER.severe(storeID + " does not exist");
            throw new Exception(storeID + " does not exist");
        }
    }

    public String deletePermission(int storeID, String ownerUserName, String userName) throws Exception {
        if (storeOwners.containsKey(storeID)) {
            if (storeOwners.get(storeID).findNode(ownerUserName).getData().getStoreOwner()) {
                if (storeOwners.get(storeID).findNode(userName) != null) {
                    if (storeOwners.get(storeID).findNode(ownerUserName).isChild(userName)
                            || ownerUserName.equals(userName)) {
                        storeOwners.get(storeID).deleteNode(userName);
                        UserController.LOGGER.info("Permission deleted from store");
                        return "Permission deleted from store";
                    } else {
                        UserController.LOGGER.severe(userName + " is not employed by " + ownerUserName);
                        throw new Exception(userName + " is not employed by " + ownerUserName);
                    }
                } else {
                    UserController.LOGGER.severe(userName + " already exists");
                    throw new Exception(userName + " already exists");
                }
            } else {
                UserController.LOGGER.severe(ownerUserName + " does not exist");
                throw new Exception(ownerUserName + " does not exist");
            }
        } else {
            UserController.LOGGER.severe(storeID + " does not exist");
            throw new Exception(storeID + " does not exist");
        }
    }

    public String deleteStoreOwner(int storeID, String userName) throws Exception {
        StoreController storeController = StoreController.getInstance();
        if (storeOwners.containsKey(storeID)) {
            Tree currTree = storeOwners.get(storeID);
            if (currTree.findNode(userName).getData().getStoreOwner()) {
                if (currTree.isRoot(storeOwners.get(storeID).findNode(userName))) {
                    storeController.deleteStore(storeID);
                    storeOwners.remove(storeID);
                    UserController.LOGGER.info("deleted main store owner - store is closed permanently");
                    return "deleted main store owner - store is closed perminantly";
                }
                storeOwners.get(storeID).deleteNode(userName);
                UserController.LOGGER.info("deleted store owner");
                return "deleted store owner";
            } else {
                UserController.LOGGER.severe(userName + " not a store owner");
                throw new Exception(userName + " not a store owner");
            }
        } else {
            UserController.LOGGER.severe(storeID + " does not exist");
            throw new Exception(storeID + " does not exist");
        }
    }

    public String suspendUser(String username) {
        suspensionInfo sI = new suspensionInfo(LocalDateTime.now(), 0);
        suspendedUsers.put(username, sI);
        return "suspended successfully";
    }

    public String suspendUserSeconds(String username, int duration) {
        suspensionInfo sI = new suspensionInfo(LocalDateTime.now(), duration);
        suspendedUsers.put(username, sI);
        scheduler.schedule(() -> resumeUser(username), duration, TimeUnit.SECONDS);
        return username + " suspended for " + duration + " seconds";
    }

    public String resumeUser(String username) {
        suspendedUsers.remove(username);
        return username + " unsuspended";
    }

    public String resumeTemporarily(String username, int duration) {
        resumeUser(username);
        scheduler.schedule(() -> suspendUser(username), duration, TimeUnit.SECONDS);
        return username + " resumed for " + duration + " seconds";
    }

    public boolean isSuspended(String username) {
        return suspendedUsers.containsKey(username);
    }

    public String viewSuspended() {
        StringBuilder result = new StringBuilder();
        for (Entry<String, suspensionInfo> entry : suspendedUsers.entrySet()) {
            result.append(entry.getKey());
            result.append(entry.getValue().toString());
        }
        return result.toString();
    }

    // this is for testing
    public void setToNull() {
        instance = null;
    }

}

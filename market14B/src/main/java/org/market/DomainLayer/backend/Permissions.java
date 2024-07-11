package org.market.DomainLayer.backend;

import org.market.DomainLayer.backend.NotificationPackage.BaseNotifier;
import org.market.DomainLayer.backend.NotificationPackage.DelayedNotifierDecorator;
import org.market.DomainLayer.backend.NotificationPackage.ImmediateNotifierDecorator;
import org.market.DomainLayer.backend.NotificationPackage.Notifier;
import org.market.DomainLayer.backend.StorePackage.Store;
import org.market.DomainLayer.backend.StorePackage.StoreController;
import org.market.DomainLayer.backend.UserPackage.User;
import org.market.DomainLayer.backend.UserPackage.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//import org.market.DataAccessLayer.DataController;

@Component
public class Permissions {
    private Map<Integer, Tree> storeOwners = new ConcurrentHashMap<>();
    private Map<String, suspensionInfo> suspendedUsers = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private BaseNotifier baseNotifier = new BaseNotifier();
    private Notifier ImmediateNotifier = new ImmediateNotifierDecorator(baseNotifier);
    private Notifier DelayerNotifier = new DelayedNotifierDecorator(baseNotifier);



//    @Autowired
//    private DataController dataController;
    @Autowired
    private StoreController storeController;

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
        while(!deleteLock.tryLock()){
            Thread.sleep(1000);
        }
        try{
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
        }finally{
            deleteLock.unlock();
        }
        
    }
    
    private final Lock addLock = new ReentrantLock();

    public String addPermission(int storeID, String ownerUserName, String userName, Boolean storeOwner,
        Boolean storeManager, Boolean[] pType) throws Exception {
        Permission permission = new Permission(userName, storeOwner, storeManager, pType);
        Node permissionNode = new Node(permission);
        if (storeOwners.containsKey(storeID)) {
            if (storeOwners.get(storeID).findNode(ownerUserName).getData().getStoreOwner()) {
                addLock.lock();
                try{
                    if (storeOwners.get(storeID).findNode(userName) == null) {
                        updateUser(userName, "you have an updated permission");
                        storeOwners.get(storeID).findNode(ownerUserName).addChild(permissionNode);
                        UserController.LOGGER.info("Permission added to store");
                        return "Permission added to store";
    
                    } else {
                        UserController.LOGGER.severe(userName + " already exists");
                        throw new Exception(userName + " already exists");
                    }
                }finally{
                    addLock.unlock();
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

    private final Lock editLock = new ReentrantLock();

    public String editPermission(int storeID, String ownerUserName, String userName, Boolean storeOwner,
            Boolean storeManager, Boolean[] pType) throws Exception {
        Permission permission = new Permission(userName, storeOwner, storeManager, pType);
        if (storeOwners.containsKey(storeID)) {
            if (storeOwners.get(storeID).findNode(ownerUserName).getData().getStoreOwner()) {
                editLock.lock();
                try{
                    if (storeOwners.get(storeID).findNode(ownerUserName).isChild(userName)) {
                        storeOwners.get(storeID).findNode(userName).edit(permission);
                        updateUser(userName, "edited your permission");
                        UserController.LOGGER.info("Permission added to store");
                        return "Permission added to store";
                    } else {
                        UserController.LOGGER.severe(userName + " is not employed by " + ownerUserName);
                        throw new Exception(userName + " is not employed by " + ownerUserName);
                    }
                }finally{
                    editLock.unlock();
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

    private final Lock deleteLock = new ReentrantLock();

    public String deletePermission(int storeID, String ownerUserName, String userName) throws Exception {
        if (storeOwners.containsKey(storeID)) {
            if (storeOwners.get(storeID).findNode(ownerUserName).getData().getStoreOwner()) {
                if (storeOwners.get(storeID).findNode(userName) != null) {
                    deleteLock.lock();
                    try{
                        if (storeOwners.get(storeID).findNode(ownerUserName).isChild(userName)
                            || ownerUserName.equals(userName)) {
                        updateUsers(storeID, ownerUserName , "deleted your permission");
                        storeOwners.get(storeID).deleteNode(userName);
                        UserController.LOGGER.info("Permission deleted from store");
                        return "Permission deleted from store";
                        } else {
                            UserController.LOGGER.severe(userName + " is not employed by " + ownerUserName);
                            throw new Exception(userName + " is not employed by " + ownerUserName);
                        }
                    }finally{
                        deleteLock.unlock();
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

    private final Lock ownerLock = new ReentrantLock();

    public String deleteStoreOwner(int storeID, String userName) throws Exception {
        //StoreController storeController = StoreController.getInstance();
        if (storeOwners.containsKey(storeID)) {
            Tree currTree = storeOwners.get(storeID);
            if (currTree.findNode(userName).getData().getStoreOwner()) {
                ownerLock.lock();
                try{
                    if (currTree.isRoot(storeOwners.get(storeID).findNode(userName))) {
                        storeController.deleteStore(storeID);
                        storeOwners.remove(storeID);
                        UserController.LOGGER.info("deleted main store owner - store is closed permanently");
                        return "deleted main store owner - store is closed perminantly";
                    }
                    storeOwners.get(storeID).deleteNode(userName);
                    UserController.LOGGER.info("deleted store owner");
                    return "deleted store owner";
                }finally{
                    ownerLock.unlock();
                }
                
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
        updateUser(username, "you were suspended, you can only view");
        return "suspended successfully";
    }

    public String suspendUserSeconds(String username, int duration) {
        suspensionInfo sI = new suspensionInfo(LocalDateTime.now(), duration);
        suspendedUsers.put(username, sI);
        scheduler.schedule(() -> resumeUser(username), duration, TimeUnit.SECONDS);
        updateUser(username, "you were suspended for " + duration + " seconds, you can only view");
        return username + " suspended for " + duration + " seconds";
    }

    public String resumeUser(String username) {
        suspendedUsers.remove(username);
        updateUser(username, "you were unsuspended");
        return username + " unsuspended";
    }

    public String resumeTemporarily(String username, int duration) {
        resumeUser(username);
        scheduler.schedule(() -> suspendUser(username), duration, TimeUnit.SECONDS);
        updateUser(username, "you were unsuspended for " + duration + " seconds");
        return username + " resumed for " + duration + " seconds";
    }

    public boolean isSuspended(String username) {
        return suspendedUsers.containsKey(username);
    }

    public String viewSuspended() {
//        if(suspendedUsers.isEmpty()){
//            ArrayList<String> suspended=(ArrayList<String>)DataController.getinstance().viewSuspended();
//            if (suspended.isEmpty()) {
//                UserController.LOGGER.info("no suspended users");
//                return "<Empty>";
//            }
//            for(String name : suspended){
//                suspendedUsers.put(name,new suspensionInfo(null, 0));
//            }
//        }
        StringBuilder result = new StringBuilder();
        for (Entry<String, suspensionInfo> entry : suspendedUsers.entrySet()) {
            result.append(entry.getKey());
            result.append(entry.getValue().toString());
        }
        return result.toString();
    }


    //notifier
    public void updateStoreOwners(int storeId, String updateMessage) {
        if (storeOwners.containsKey(storeId)) {
            Tree tree = storeOwners.get(storeId);
            for (String username : tree) {
                chooseNotifier(username,updateMessage);
            }
        }
        else {
            UserController.LOGGER.severe("store " + storeId + " does not exist");
        }
    }

    public void updateUsers(int storeId, String root, String updateMessage) {
        Iterator<String> iterator = storeOwners.get(storeId).subtreeIterator(root);
        if (iterator != null) {
            while (iterator.hasNext()) {
                updateUser(iterator.next(), updateMessage);
            }
        }
    }

    public void updateUser(String username, String updateMessage){
        chooseNotifier(username,updateMessage);
    }

    private void chooseNotifier(String username, String message) {
        UserController userController = UserController.getInstance();
        User user = userController.getUser(username);
        if (user.isLoggedIn()) {
            ImmediateNotifier.send(username, message);
            UserController.LOGGER.info("message sent to " + username);
        }
        else {
            DelayerNotifier.send(username, message);
            UserController.LOGGER.info("message stacked to " + username);
        }
    }


    // this is for testing
    public void setToNull() {
        instance = null;
    }

    public double reviewOffer(int storeId, double price, int productId) throws Exception {
        if(storeOwners.containsKey(storeId)){
            String rootUserName=storeOwners.get(storeId).getRoot().getData().getUserName();
            updateUsers(storeId, rootUserName, "review this offer");
            Iterator<String> iterator = storeOwners.get(storeId).subtreeIterator(rootUserName);
            Boolean response=true;
            if (iterator != null) {
                while (iterator.hasNext()) {
                    response&=UserController.getInstance().reviewOffer(price, iterator.next());
                }
            }
            if(response)
                return price;
            else{
                return storeController.getStore(storeId).getProdPrice(productId);
            }
        }
        else{
            UserController.LOGGER.severe(storeId + " does not exist");
            throw new Exception(storeId + " does not exist");
        }
    }

    public List<Store> getUserStores(String username) throws Exception {
        List<Store> stores = new ArrayList<>();
        for(int strId: storeOwners.keySet()){
            if(storeOwners.get(strId).findNode(username) != null) {
                Permission permission = getPermission(strId, username);
                if (permission.getStoreManager() || permission.getStoreOwner()) {
                    stores.add(storeController.getStore(strId));
                }
            }
        }
        return stores;
    }

    public Map<Integer, Permission> getUserPermissions(String username) throws Exception{
        Map<Integer, Permission> map = new HashMap<>();
        for(int strId: storeOwners.keySet()){
            if(storeOwners.get(strId).findNode(username) != null){
                Permission permission = getPermission(strId, username);
                map.put(strId, permission);
            }
        }
        return map;
    }

}

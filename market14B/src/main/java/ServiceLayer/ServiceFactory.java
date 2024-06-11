package ServiceLayer;

public class ServiceFactory {
    private MarketService marketService = new MarketService();
    private UserService userService = new UserService();
    private StoresService storesService = new StoresService();

    private static ServiceFactory instance;

    public static synchronized ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    // MarketService
    public void setMarketOnline(String username) throws Exception {
        marketService.setMarketOnline(username);
    }

    public void setMarketOFFLINE(String username) throws Exception {
        marketService.setMarketOFFLINE(username);
    }

    // UserService
    public String EnterAsGuest() {
        return userService.EnterAsGuest();
    }

    public String GuestExit(String username) {
        return userService.GuestExit(username);
    }

    public String Login(String guest, String username, String password) {
        return userService.Login(guest, username, password);
    }

    public String Logout(String username) {
        return userService.Logout(username);
    }

    public String Register(String username, String password) {
        return userService.Register(username, password);
    }

    public String Buy(String username) {
        return userService.Buy(username);
    }

    public String addToCart(String username, Integer product, int storeId, int quantity) {
        return userService.addToCart(username, product, storeId, quantity);
    }

    public String inspectCart(String username) {
        return userService.inspectCart(username);
    }

    public String removeCartItem(String username, int storeId, int product) {
        return userService.removeCartItem(username, storeId, product);
    }

    public String EditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner,
            Boolean storeManager, Boolean[] pType) throws Exception {
        return userService.EditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
    }

    public String AssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
        return userService.AssignStoreManager(storeId, ownerUserName, username, pType);
    }

    public String AssignStoreOnwer(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
        return userService.AssignStoreOnwer(storeId, ownerUserName, username, pType);
    }

    public String unassignUser(int storeID, String ownerUserName, String userName) throws Exception {
        return userService.unassignUser(storeID, ownerUserName, userName);
    }

    // StoreService
    public String initStore(String userName, String Description) throws Exception {
        return storesService.initStore(userName, Description);
    }

    public String addProduct(int productId, int storeId, double price, int quantity, String username) throws Exception {
        return storesService.addProduct(productId, storeId, price, quantity, username);
    }

    public String removeProduct(int productId, int storeId, String username) {
        return storesService.removeProduct(productId, storeId, username);
    }

    public String EditProducPrice(int productId, int storeId, Double newPrice, String username) throws Exception {
        return storesService.EditProducPrice(productId, storeId, newPrice, username);
    }

    public String EditProductQuantity(int productId, int storeId, int newQuantity, String username) throws Exception {
        return storesService.EditProductQuantity(productId, storeId, newQuantity, username);
    }

    public String closeStore(int storeId, String username) {
        return storesService.closeStore(storeId, username);
    }

    public String openStore(int storeId, String username) {
        return storesService.openStore(storeId, username);
    }

    public String getInfo(int storeId, String username) {
        return storesService.getInfo(storeId, username);
    }
}

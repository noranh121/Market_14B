package main.Web.Requests;

public class cartOp {
    private String username;
    private Integer ProductId;
    private Integer StoreId;
    private Integer Quantity;


    public cartOp(String username,Integer quantity,Integer prodid,Integer storeid){
        this.username = username;
        this.ProductId = prodid;
        this.StoreId = storeid;
        this.Quantity = quantity;
    }

    public String getUsername(){
        return this.username;
    }
    public Integer getProdID(){
        return this.ProductId;
    }
    public Integer getStoreID(){
        return this.StoreId;
    }
    public Integer getQuant(){
        return this.Quantity;
    }
    
}

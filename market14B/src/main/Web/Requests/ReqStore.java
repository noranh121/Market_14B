package main.Web.Requests;


public class ReqStore {
    private String username;
    private Integer ProductId;
    private Integer StoreId;
    private Integer Quantity;
    private Double price;


    public ReqStore(String username,Integer prodid,Integer storeid, Integer quant,Double price){
        this.username = username;
        this.ProductId = prodid;
        this.StoreId = storeid;
        this.Quantity = quant;
        this.price = price;
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
    public Integer getQuantity(){
        return this.Quantity;
    }
    public Double getPrice(){
        return this.price;
    }
    
}

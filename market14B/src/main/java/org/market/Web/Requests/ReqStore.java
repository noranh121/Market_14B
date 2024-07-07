package org.market.Web.Requests;


public class ReqStore {
    private String username;
    private Integer ProductId;
    private Integer StoreId;
    private Integer Quantity;
    private Double price;
    private Double weight;


    public ReqStore(String username,Integer prodid,Integer storeid, Integer quant,Double price, Double weight){
        this.username = username;
        this.ProductId = prodid;
        this.StoreId = storeid;
        this.Quantity = quant;
        this.price = price;
        this.weight = weight;
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

    public Double getWeight(){
        return this.weight;
    }
    
}

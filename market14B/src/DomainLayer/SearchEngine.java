package DomainLayer;

import java.util.List;

import DomainLayer.backend.ISearchEngine;

public class SearchEngine implements ISearchEngine{
    private boolean prodRating;
    private boolean priceRange;
    private boolean category;
    private boolean storeRating;


    public SearchEngine(){
        this.prodRating = false;
        this.priceRange = false;
        this.category = false;
        this.storeRating = false;
    }

    //ONLY FOR NOW ==> functions recive a single argument and return a result in List<String> state (could be modified later)
    // public List<String> HandleResult(List<String> result){
    //     //        if(inStore) //storeID needed  
    //         //{ trim results to only store products, apply filters to the result}
    //         //apply filters.
    // public List<String> searchbyProdName(String prodName){

    // }
    // public List<String> searchbyCategory(String CategoryName){
        
    // }
    // public List<String> searchbyKeyWord(String KeyWord){
        
    // }

    public void setprodRating(){
        this.prodRating = true;
    }
    public void setpriceRange(){
        this.priceRange = true;
    }
    public void setcategory(){
        this.category = true;
    }

    public void setstoreRating(){
        this.category = true;
    }





}

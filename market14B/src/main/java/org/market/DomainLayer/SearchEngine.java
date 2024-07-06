package org.market.DomainLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.market.DomainLayer.backend.ISearchEngine;
import org.market.DomainLayer.backend.ProductPackage.CategoryController;
import org.market.DomainLayer.backend.ProductPackage.Product;
import org.market.DomainLayer.backend.ProductPackage.ProductController;
import org.market.Web.Requests.SearchEntity;

public class SearchEngine implements ISearchEngine {

    Map<String, List<Integer>> keyword_srch;// keyword ==> List of product ID's

    public SearchEngine(){
        this.keyword_srch = new ConcurrentHashMap<>();
    }


    public void HandleSearch(SearchEntity entity){
        String searchBy = entity.getSearchBy();
        List<Product> result = new ArrayList<>();
        switch (searchBy) {
            case "key":
                result = searchbyKeyWord(entity.getKeyword());
                break;
            case "prod":
                result = searchbyProdName(entity.getProductName());
                break;
            case "cat":
                result = searchbyCategory(entity.getCategoryName());
                break;
            default:
                //here no legal search method entered, throw exception to handle message to the client
                //most likely won't happen!
                break;
        }
        result = HandleResult(result, entity);

    }

    //ONLY FOR NOW ==> functions recive a single argument and return a result in List<String> state (could be modified later)
    public List<Product> HandleResult(List<Product> result, SearchEntity entity){
        //if(inStore){}
        //else{
        if(entity.isRange()){
            Double min = entity.getMin();
            Double max = entity.getMax();
            //trim result to min max
        }

        if(entity.isProdRating()){
            Comparator<Product> ratingComparator = new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return Double.compare(p1.getRating(), p2.getRating());
                }
            };
            Collections.sort(result, ratingComparator);
        }
        
        if(entity.isCategory()){

        }

        return null;
    }


    public List<Product> searchbyProdName(String prodName){
        //if(inStore){}
        //else{
        List<Product> result = ProductController.getInstance().getProducts()
        .stream()
            .filter(product -> product.getName().contains(prodName))
            .collect(Collectors.toList());
        return result;
    }
    public List<Product> searchbyCategory(String CategoryName){
        //if(inStore){}
        //else{
        List<Integer> ids = CategoryController.getinstance().getCategorybyName(CategoryName).getAllProductIds();
        List<Product> result = ProductController.getInstance().getProductsByIDs(ids);
        return result;
    }
    public List<Product> searchbyKeyWord(String KeyWord){
        //if(inStore){}
        //else{
        List<Product> result = ProductController.getInstance().getProductsByIDs(keyword_srch.get(KeyWord));
        return result;
    }


}

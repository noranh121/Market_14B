package org.market.DomainLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.market.DomainLayer.backend.ISearchEngine;
import org.market.DomainLayer.backend.Market;
import org.market.DomainLayer.backend.ProductPackage.CategoryController;
import org.market.DomainLayer.backend.ProductPackage.Product;
import org.market.DomainLayer.backend.ProductPackage.ProductController;
import org.market.Web.DTOS.ProductDTO;
import org.market.Web.Requests.SearchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchEngine implements ISearchEngine {

    private static Map<String, List<Integer>> keyword_srch;// keyword ==> List of product ID's

    public SearchEngine(){
        this.keyword_srch = new ConcurrentHashMap<>();
    }

    public static void addToKeyWords(String keyword, int prodID){
        keyword_srch.computeIfAbsent(keyword, k -> new ArrayList<>()).add(prodID);
    }


    public List<ProductDTO> HandleSearch(SearchEntity entity){
        String searchBy = entity.getSearchBy();
        List<ProductDTO> result = new ArrayList<>();
        switch (searchBy) {
            case "key":
                result = searchbyKeyWord(entity.getKeyword(),entity);
                break;
            case "prod":
                result = searchbyProdName(entity.getProductName(),entity);
                break;
            case "cat":
                result = searchbyCategory(entity.getCategoryName(),entity);
                break;
            default:
                break;
        }
        HandleResult(result, entity);
        return result;

    }

    public void HandleResult(List<ProductDTO> result, SearchEntity entity){
        if(entity.isRange()){
            Double min = entity.getMin();
            Double max = entity.getMax();
            result = result.stream()
                           .filter(product -> product.getPrice() >= min && product.getPrice() <= max)
                           .collect(Collectors.toList());
        }

        if(entity.isProdRating()){
            Comparator<ProductDTO> ratingComparator = new Comparator<ProductDTO>() {
                @Override
                public int compare(ProductDTO p1, ProductDTO p2) {
                    return Double.compare(p1.getRating(), p2.getRating());
                }
            };
            Collections.sort(result, ratingComparator);
        }
        
        if(entity.isCategory()){
            result.sort(Comparator.comparing(ProductDTO::getCategory));
        }
    }


    public List<ProductDTO> searchbyProdName(String prodName, SearchEntity entity){
        List<ProductDTO> prodsDTO = new ArrayList<>();
        if(entity.isInStore()){
            int storeID = entity.getStoreID();
            prodsDTO = Market.getSC().getStoreProducts(storeID); 
        }else{
        List<Product> result = Market.getPC().getProducts()
        .stream()
            .filter(product -> product.getName().contains(prodName))
            .collect(Collectors.toList());
        prodsDTO = Market.convertProds(result);
        }
        return prodsDTO;
    }
    public List<ProductDTO> searchbyCategory(String CategoryName,SearchEntity entity){
        List<ProductDTO> pdtos = new ArrayList<>();
        List<Integer> ids = Market.getCC().getCategorybyName(CategoryName).getAllProductIds();
        List<Product> result = Market.getPC().getProductsByIDs(ids);
        if(entity.isInStore()){
            pdtos = Market.convertProds(result,entity.getStoreID());
        }else{
            pdtos = Market.convertProds(result);
        }
        return pdtos;
    }
    public List<ProductDTO> searchbyKeyWord(String KeyWord,SearchEntity entity){
        List<ProductDTO> pdtos = new ArrayList<>();
        List<Product> result = Market.getPC().getProductsByIDs(keyword_srch.get(KeyWord));
        if(entity.isInStore()){
            pdtos = Market.convertProds(result,entity.getStoreID());
        }else{
            pdtos = Market.convertProds(result);
        }
        return pdtos;
    }


}

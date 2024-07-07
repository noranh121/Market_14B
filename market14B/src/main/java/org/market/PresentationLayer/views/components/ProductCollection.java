package org.market.PresentationLayer.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.market.PresentationLayer.presenter.ProductCollectionPresenter;
import org.market.Web.DTOS.ProductDTO;

import java.util.List;

public class ProductCollection extends HorizontalLayout {

    private ProductCollectionPresenter prensenter;
    List<VerticalLayout> products;

    public ProductCollection(String store_id){
        addClassName("collection-layout");
        setSpacing(true);
        setPadding(true);

        this.prensenter = new ProductCollectionPresenter(this, store_id);
    }

    public void loadProducts(List<ProductDTO> products){
        add(createProductCard("Headphones 1", "20.00" ,"icons/headphones.jpg" ));
        add(createProductCard("Headphones 2", "30.00" ,"icons/headphones.jpg" ));
        add(createProductCard("Headphones 3", "40.00" ,"icons/headphones.jpg" ));
        add(createProductCard("Headphones 4", "50.00" ,"icons/headphones.jpg" ));
        add(createProductCard("Headphones 5", "50.00" ,"icons/headphones.jpg" ));
        add(createProductCard("Headphones 6", "50.00" ,"icons/headphones.jpg" ));
        add(createProductCard("Headphones 7", "50.00" ,"icons/headphones.jpg" ));
        add(createProductCard("Headphones 8", "50.00" ,"icons/headphones.jpg" ));
        add(createProductCard("Headphones 4", "50.00" ,"icons/headphones.jpg" ));
        add(createProductCard("Headphones 5", "50.00" ,"icons/headphones.jpg" ));
        add(createProductCard("Headphones 6", "50.00" ,"icons/headphones.jpg" ));
        add(createProductCard("Headphones 7", "50.00" ,"icons/headphones.jpg" ));
        add(createProductCard("Headphones 8", "50.00" ,"icons/headphones.jpg" ));
    }

    private VerticalLayout createProductCard(String title, String price, String url){
        VerticalLayout productCard = new VerticalLayout();
        productCard.addClassName("collection-card");


        Image productImage = new Image(url, title);
        productImage.addClassName("image");

        Span productTitle = new Span(title);
        productTitle.addClassName("title");

        Span productPrice = new Span("$" + price);
        productPrice.addClassName("price");

        HorizontalLayout info = new HorizontalLayout();
        HorizontalLayout filler = new HorizontalLayout();

        info.add(productTitle);
        info.addAndExpand(filler);
        info.add(productPrice);
        info.addClassName("info");

        productCard.add(productImage, info);
        productCard.addClickListener(e-> UI.getCurrent().navigate("/dash/product/" + title));
        return productCard;
    }


}

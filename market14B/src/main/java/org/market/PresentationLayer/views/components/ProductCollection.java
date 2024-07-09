package org.market.PresentationLayer.views.components;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.market.PresentationLayer.presenter.ProductCollectionPresenter;
import org.market.Web.DTOS.ProductDTO;

import java.util.ArrayList;
import java.util.List;

public class ProductCollection extends HorizontalLayout {

    private int store_id;

    private ProductCollectionPresenter presenter;
    List<VerticalLayout> products;

    public ProductCollection(int store_id){

        this.store_id = store_id;
        addClassName("collection-layout");
        setSpacing(true);
        setPadding(true);

        this.presenter = new ProductCollectionPresenter(this, store_id);
    }

    public void loadProducts(ArrayList<ProductDTO> products) {
        removeAll();
        for (ProductDTO product : products) {
            ProductCard card = new ProductCard(product, this.store_id != -1);
            add(card);
        }
    }

}

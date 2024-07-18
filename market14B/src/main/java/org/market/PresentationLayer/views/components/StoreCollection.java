package org.market.PresentationLayer.views.components;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.market.PresentationLayer.presenter.StoreCollectionPresenter;
import org.market.Web.DTOS.StoreDTO;

import java.util.ArrayList;

public class StoreCollection extends HorizontalLayout {

    private StoreCollectionPresenter presenter;

    public StoreCollection(){
        addClassName("collection-layout");
        setSpacing(true);
        setPadding(true);

        this.presenter = new StoreCollectionPresenter(this);
    }

    public void loadStores(ArrayList<StoreDTO> stores) {
        removeAll();
        for(StoreDTO store : stores){
            StoreCard card = new StoreCard(store);
            add(card);
        }
    }
}

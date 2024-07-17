package org.market.PresentationLayer.views.components;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.presenter.PurchaseHistoryPresenter;

import java.util.List;

public class PurchaseHistory extends VerticalLayout implements BeforeEnterObserver{

    private int store_id;
    private VerticalLayout purchaseLayout;
    private IntegerField deleteField;
    private Button deleteButton;
    private PurchaseHistoryPresenter presenter;

    public PurchaseHistory(int store_id) {
        this.store_id = store_id;
        setTopBar();
        this.presenter = new PurchaseHistoryPresenter(this);
    }

    private void setTopBar() {
        this.deleteField = new IntegerField("Purchase ID:","Purchase ID...");
        this.deleteButton = new Button("Delete");
        deleteButton.addClassName("delete-purchase-btn");

        HorizontalLayout delete_layout = new HorizontalLayout(deleteField, deleteButton);
        delete_layout.addClassName("purchase-top-bar");
        delete_layout.setWidthFull();

        add(delete_layout);

        this.purchaseLayout = new VerticalLayout();
        add(purchaseLayout);
    }

    public void createPurchaseHistoryLayout(List<String> purchases) {
        this.purchaseLayout.removeAll();
        Span title = new Span("Purchase History:");
        title.addClassName("purchase-title");
        this.purchaseLayout.add(title);

        for (String purchase : purchases) {
            String htmlText = purchase.replace("\n", "<br>");
            Span purchase_info = new Span();
            purchase_info.getElement().setProperty("innerHTML", htmlText);

            HorizontalLayout layout = new HorizontalLayout();
            layout.setWidthFull();
            layout.add(purchase_info);
            layout.addClassName("purchase-item-layout");
            this.purchaseLayout.add(layout);
        }
    }

    public void setDeleteClickEventListener(ComponentEventListener<ClickEvent<Button>> e) {
        this.deleteButton.addClickListener(e);
    }

    public IntegerField getDeleteField() {
        return deleteField;
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String username = (String) VaadinSession.getCurrent().getAttribute("current-user");

        if (username == null) {
            event.rerouteTo("login");
        } else {
            presenter.loadPurchaseHistory();
        }
    }


    public int getStore_id() {
        return store_id;
    }
}

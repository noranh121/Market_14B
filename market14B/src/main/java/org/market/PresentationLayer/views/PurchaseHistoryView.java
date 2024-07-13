package org.market.PresentationLayer.views;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.presenter.PurchaseHistoryPresenter;

import java.util.List;

@Route(value = "purchase-history", layout = HomeView.class)
@RoutePrefix("dash")
public class PurchaseHistoryView extends VerticalLayout implements BeforeEnterObserver{

    private VerticalLayout purchaseLayout;
    private IntegerField deleteField;
    private Button deleteButton;
    private PurchaseHistoryPresenter presenter;

    public PurchaseHistoryView() {
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

        addClassName("purchase-view");
    }

    public void createPurchaseHistoryLayout(List<String> purchases) {
        this.purchaseLayout.removeAll();
        Span title = new Span("Purchase History:");
        title.addClassName("purchase-title");
        this.purchaseLayout.add(title);

        for (String purchase : purchases) {
            HorizontalLayout layout = new HorizontalLayout();
            layout.setWidthFull();
            Span purchase_info = new Span(purchase);
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

//    private void createTopBar() {
//        this.topBar = new HorizontalLayout();
//        this.topBar.addClassName("purchase-history-top-bar");
//        topBar.setWidthFull();
//
//        Span title = new Span("Purchase History");
//        title.addClassName("title");
//
//        this.removeField = new IntegerField("remove purchase by ID");
//        this.removeButton = new com.vaadin.flow.component.button.Button();
//        this.removeButton.addClassName("remove-button");
//
//        HorizontalLayout removelayout = new HorizontalLayout(removeField, removeButton);
//        removelayout.addClassName("remove-layout");
//
//        topBar.add(title, removelayout);
//        add(topBar);
//    }
//
//    public void createPurchasesLayout(List<String> purchases) {
//        this.purchaseLayout.removeAll();
//        this.purchaseLayout.add(new Span("Purchases:"));
//
//        if (purchases != null) {
//            for (int i = 0 ; i<purchases.size(); i++) {
//                String purchase = purchases.get(i);
//                Span span = new Span("ID: " + i + " | " + purchase);
//                this.purchaseLayout.add(span);
//            }
//        }
//    }
//
//    public void setRemoveClickEventListener(ComponentEventListener<ClickEvent<Button>> e) {
//        this.removeButton.addClickListener(e);
//    }
//
//    public IntegerField getRemoveField() {
//        return removeField;
//    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String username = (String) VaadinSession.getCurrent().getAttribute("current-user");

        if (username == null) {
            event.rerouteTo("login");
        } else {
            presenter.loadPurchaseHistory();
        }
    }




}

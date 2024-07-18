package org.market.PresentationLayer.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.market.PresentationLayer.presenter.MyStoresPresenter;
import org.market.PresentationLayer.views.components.MyStoreCard;
import org.market.PresentationLayer.views.components.SearchBar;
import org.market.Web.DTOS.StoreDTO;

import java.util.List;

@Route(value = "mystores", layout = HomeView.class)
@RoutePrefix("dash")
@AnonymousAllowed
public class MyStoresView extends VerticalLayout implements BeforeEnterObserver {

    private Dialog add_store_dialog;
    private TextField name_field;
    private TextField description_field;
    private Button add_store_btn;
    private Button apply_btn;
    private VerticalLayout stores_layout;

    private MyStoresPresenter presenter;

    public MyStoresView() {

        addClassName("scrollable-layout");

        HorizontalLayout topBar = new HorizontalLayout();
        topBar.addClassName("top-bar");

        SearchBar searchBar = new SearchBar( null, null, false, false, false, -1);
        this.add_store_btn = new Button("Add Store");
        add_store_btn.setSuffixComponent(VaadinIcon.PLUS_CIRCLE_O.create());
        add_store_btn.addClassName("add-store-btn");

        VerticalLayout dialog_layout = new VerticalLayout();

        this.name_field = new TextField("Name");
        this.name_field.setRequired(true);
        this.name_field.setRequiredIndicatorVisible(true);

        this.description_field = new TextField("Description");
        this.description_field.setRequired(true);
        this.description_field.setRequiredIndicatorVisible(true);

        HorizontalLayout apply_layout = new HorizontalLayout();
        this.apply_btn = new Button("Add");
        apply_layout.add(apply_btn);
        apply_btn.addClassName("store-apply-btn");
        apply_layout.addClassName("store-apply-layout");

        dialog_layout.add(name_field, description_field, apply_layout);
        this.add_store_dialog = new Dialog(dialog_layout);
        this.add_store_btn.addClickListener(e -> add_store_dialog.open());

        topBar.add(searchBar);
        topBar.add(add_store_btn);

        add(topBar);

        this.stores_layout = new VerticalLayout();

        add(stores_layout);

        this.presenter = new MyStoresPresenter(this);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String username = (String) VaadinSession.getCurrent().getAttribute("current-user");

        if (username == null) {
            event.rerouteTo("login");
        }
        else{
            presenter.loadStores();
        }
    }

    public void setApplyButtonClickEventListener(ComponentEventListener<ClickEvent<Button>> event) {
        this.apply_btn.addClickListener(event);
    }

    public void loadMyStores(List<StoreDTO> stores) {
        this.stores_layout.removeAll();
        for(StoreDTO store : stores){
            MyStoreCard card = new MyStoreCard(store);
            stores_layout.add(card);
        }
    }

    public TextField getName_field() {
        return name_field;
    }

    public TextField getDescription_field() {
        return description_field;
    }
}

package org.market.PresentationLayer.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.market.PresentationLayer.models.Store;
import org.market.PresentationLayer.presenter.MyStoresPresenter;
import org.market.PresentationLayer.views.components.MyStoreCard;
import org.market.PresentationLayer.views.components.SearchBar;

import java.util.Arrays;
import java.util.List;

@Route(value = "mystores", layout = HomeView.class)
@RoutePrefix("dash")
@AnonymousAllowed
public class MyStoresView extends VerticalLayout implements BeforeEnterObserver {

    private Button add_store_btn;

    private MyStoresPresenter presenter;

    public MyStoresView() {

        addClassName("scrollable-layout");

        HorizontalLayout topBar = new HorizontalLayout();
        topBar.addClassName("top-bar");

        SearchBar searchBar = new SearchBar(false);
        this.add_store_btn = new Button("Add Store");
        add_store_btn.setSuffixComponent(VaadinIcon.PLUS_CIRCLE_O.create());
        add_store_btn.addClassName("add-store-btn");

        topBar.add(searchBar);
        topBar.add(add_store_btn);

        add(topBar);

        List<Store> stores = Arrays.asList(
                new Store("Store 1", "Description 1"),
                new Store("Store 2", "Description 2"),
                new Store("Store 3", "Description 3"),
                new Store("Store 4", "Description 4"),
                new Store("Store 5", "Description 5"),
                new Store("Store 6", "Description 6")
        );

        VerticalLayout storesLayout = new VerticalLayout();

        for (Store store : stores) {
            storesLayout.add(new MyStoreCard(store));
        }

        add(storesLayout);

        this.presenter = new MyStoresPresenter(this);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String username = (String) VaadinSession.getCurrent().getAttribute("current-user");

        if (username == null) {
            event.rerouteTo("login");
        }
    }

    public void setAddStoreButtonEventListener(ComponentEventListener<ClickEvent<Button>> event) {
        this.add_store_btn.addClickListener(event);
    }
}

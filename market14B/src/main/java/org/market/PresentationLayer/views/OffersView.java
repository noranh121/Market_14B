package org.market.PresentationLayer.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import org.market.PresentationLayer.presenter.OffersPresenter;
import org.market.Web.DTOS.OfferDTO;

import java.util.List;

@Route(value = "offers", layout = HomeView.class)
@RoutePrefix("dash")
public class OffersView extends VerticalLayout implements HasUrlParameter<Integer> {

    private VerticalLayout offersLayout;
    private Integer store_id;
    private OffersPresenter presenter;

    public OffersView(){
        this.offersLayout = new VerticalLayout();
        add(offersLayout);
    }


    public void createViewOffersLayout(List<OfferDTO> offers) {
        this.offersLayout.removeAll();
        Span title = new Span("Open Offers:");
        title.addClassName("offer-title");
        this.offersLayout.add(title);

        for (OfferDTO offer : offers) {
            String offerItem = "Name: " + offer.getName() + ", " +
                    "Id: " + offer.getProductId() + ", " +
                    "Price: " + offer.getPrice() + ", " +
                    "Offer: " + offer.getOffer();

            String htmlText = offerItem.replace("\n", "<br>");
            Span offer_info = new Span();
            offer_info.getElement().setProperty("innerHTML", htmlText);

            HorizontalLayout layout = new HorizontalLayout();
            layout.setWidthFull();
            layout.add(offer_info);
            layout.addClassName("offers-layout");

            Button approveBtn = new Button(VaadinIcon.CHECK.create());
            approveBtn.addClassName("approve-btn");
            approveBtn.getStyle().set("border-radius", "50%");
            approveBtn.addClickListener(e -> this.presenter.approveOffer(this.store_id, offer.getProductId(), offer.getUsername(), offer.getPrice()));

            Button disapproveBtn = new Button(VaadinIcon.CLOSE.create());
            disapproveBtn.addClassName("disapprove-btn");
            disapproveBtn.getStyle().set("border-radius", "50%");
            disapproveBtn.addClickListener(e -> this.presenter.disapproveOffer(this.store_id, offer.getProductId(), offer.getUsername()));

            layout.add(approveBtn, disapproveBtn);
            layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

            this.offersLayout.add(layout);
        }
    }

    public Integer getStore_id() {
        return store_id;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer integer) {
        this.store_id = integer;
        this.presenter = new OffersPresenter(this);
    }
}

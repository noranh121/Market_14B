package org.market.PresentationLayer.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.List;

@Route(value = "offers", layout = HomeView.class)
@RoutePrefix("dash")
public class OffersView extends VerticalLayout {

    private VerticalLayout offersLayout;

    public  OffersView(){
    }


    public void createViewOffersLayout(List<String> offers) {
        this.offersLayout.removeAll();
        Span title = new Span("Open Offers:");
        title.addClassName("offer-title");
        this.offersLayout.add(title);

        for (String offer : offers) {
            String htmlText = offer.replace("\n", "<br>");
            Span offer_info = new Span();
            offer_info.getElement().setProperty("innerHTML", htmlText);

            HorizontalLayout layout = new HorizontalLayout();
            layout.setWidthFull();
            layout.add(offer_info);
            layout.addClassName("offers-layout");

            Button approveBtn = new Button(VaadinIcon.CHECK.create());
            approveBtn.addClassName("approve-btn");
            approveBtn.getStyle().set("border-radius", "50%");

            Button disapproveBtn = new Button(VaadinIcon.CLOSE.create());
            disapproveBtn.addClassName("disapprove-btn");
            disapproveBtn.getStyle().set("border-radius", "50%");

            layout.add(approveBtn, disapproveBtn);
            layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

            this.offersLayout.add(layout);
        }
    }
    
}

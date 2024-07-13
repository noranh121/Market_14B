package org.market.PresentationLayer.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.presenter.SuspendPresenter;

@Route(value = "suspensions", layout = HomeView.class)
@RoutePrefix("dash")
public class SuspendView extends VerticalLayout implements BeforeEnterObserver {

    private HorizontalLayout topBar;
    private VerticalLayout suspendedLayout;
    private TextField userField;
    private Checkbox temporary;
    private Checkbox indefinitely;
    private IntegerField timeField;
    private Button suspend_btn;
    private Button resume_btn;
    private SuspendPresenter presenter;


    public SuspendView(){
        createTopBar();
        this.presenter = new SuspendPresenter(this);
    }

    public void createTopBar() {
        this.topBar = new HorizontalLayout();
        this.topBar.addClassName("manager-settings-top-bar");
        topBar.setWidthFull();

        this.userField = new TextField("Username", "User to be suspended...");
        this.timeField = new IntegerField("Duration", "Duration in seconds...");
        this.temporary = new Checkbox("Temporary");
        this.indefinitely = new Checkbox("Indefinitely");

        this.temporary.addValueChangeListener(e -> {
            if (e.getValue()) this.indefinitely.setValue(false);
        });
        this.indefinitely.addValueChangeListener(e -> {
            if (e.getValue()) this.temporary.setValue(false);
        });

        topBar.add(userField, timeField, temporary, indefinitely);
        add(topBar);

        HorizontalLayout btn_layout = new HorizontalLayout();

        this.suspend_btn = new Button("Suspend");
        this.suspend_btn.addClassName("manager-setting-btn");

        this.resume_btn = new Button("Resume");
        this.resume_btn.addClassName("manager-setting-btn");


        btn_layout.add(suspend_btn);
        btn_layout.add(resume_btn);

        topBar.add(btn_layout);
    }

    public void createSuspendedUsersLayout(String suspended){
        this.suspendedLayout = new VerticalLayout();
        this.suspendedLayout.add(new Span("Suspended Users:"));

        if(suspended != null) {
            // Replace \n with <br> for HTML rendering
            String htmlText = suspended.replace("\n", "<br>");

            // Or using Span with HTML
            Span span = new Span();
            span.getElement().setProperty("innerHTML", htmlText);
            this.suspendedLayout.add(span);
        }

        add(suspendedLayout);
    }

    public void setSuspendClickEventListener(ComponentEventListener<ClickEvent<Button>> e){
        this.suspend_btn.addClickListener(e);
    }

    public void setResumeClickEventListener(ComponentEventListener<ClickEvent<Button>> e){
        this.resume_btn.addClickListener(e);
    }

    public TextField getUserField() {
        return userField;
    }

    public Checkbox getTemporary() {
        return temporary;
    }

    public Checkbox getIndefinitely() {
        return indefinitely;
    }

    public IntegerField getTimeField() {
        return timeField;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String username = (String) VaadinSession.getCurrent().getAttribute("current-user");

        if (username == null) {
            event.rerouteTo("login");
        }
        else{
            presenter.loadSuspendedUsers();
        }
    }
}

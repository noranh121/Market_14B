package org.market.PresentationLayer.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.handlers.PermissionHandler;
import org.market.PresentationLayer.presenter.ManagerSettingPresenter;
import org.market.Web.DTOS.StoreDTO;

@Route(value = "settings", layout = HomeView.class)
@RoutePrefix("dash")
public class ManagerSettingsView extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {

    private int store_id;
    private StoreDTO store;
    private HorizontalLayout topBar;
    private Div assignRoleSection;
    private Div permissionSection;
    private ManagerSettingPresenter presenter;
    private TextField roleField;
    private Checkbox isOwner;
    private Checkbox isManager;
    private Checkbox editProducts;
    private Checkbox editPurchaseHistory;
    private Checkbox editDiscountHistory;
    private Button assign_btn;
    private Button update_btn;
    private Button unassign_btn;
    private Button resign_btn;



    public ManagerSettingsView() {
        addClassName("manager-settings-view");

        this.topBar = new HorizontalLayout();
        add(topBar);

        this.assignRoleSection = new Div();
        this.permissionSection = new Div();

        HorizontalLayout mainContent = new HorizontalLayout(assignRoleSection, permissionSection);
        mainContent.addClassName("manager-settings-main-content");
        add(mainContent);
    }

    public void createTopBar() {
        this.topBar.addClassName("manager-settings-top-bar");
        topBar.setWidthFull();

        String username = VaadinSession.getCurrent().getAttribute("current-user").toString();
        HorizontalLayout nameLayout = createLabelWithValue("Name:", username);
        HorizontalLayout roleLayout = createLabelWithValue("Role:", PermissionHandler.getRole(this.store_id));
        HorizontalLayout storeNameLayout = createLabelWithValue("Store Name:", this.store.getName());

        topBar.add(nameLayout, roleLayout, storeNameLayout);
    }

    private HorizontalLayout createLabelWithValue(String label, String value) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassName("manager-setting-label-value-layout");

        Span labelComponent = new Span(label);
        labelComponent.addClassName("manager-setting-label");
        Span valueComponent = new Span(value);
        valueComponent.addClassName("manager-setting-value");

        layout.add(labelComponent, valueComponent);
        return layout;
    }

    public void createAssignRoleSection(String sectionTitle) {

        this.assignRoleSection.addClassName("manager-settings-role-section");

        H2 title = new H2(sectionTitle);
        this.assignRoleSection.add(title);
        title.getStyle().set("padding", "10px");

        this.roleField = new TextField("Username");
        this.isOwner = new Checkbox("Owner");
        this.isManager = new Checkbox("Manager");
        this.isOwner.addValueChangeListener(e -> {
            if (e.getValue()) this.isManager.setValue(false);
        });
        this.isManager.addValueChangeListener(e -> {
            if (e.getValue()) this.isOwner.setValue(false);
        });

        HorizontalLayout roleLayout = new HorizontalLayout(this.roleField, this.isOwner, this.isManager);
        roleLayout.addClassName("manager-setting-role-layout");
        this.assignRoleSection.add(roleLayout);

        VerticalLayout textWithCheckboxes = new VerticalLayout();

        Span span1 = new Span("Edit products");
        this.editProducts = new Checkbox();
        HorizontalLayout textCheckboxLayout1 = new HorizontalLayout();
        textCheckboxLayout1.add(span1);
        textCheckboxLayout1.addAndExpand(new HorizontalLayout());
        textCheckboxLayout1.add(this.editProducts);
        textWithCheckboxes.add(textCheckboxLayout1);
        this.assignRoleSection.add(textWithCheckboxes);

        Span span2 = new Span("Add or Edit Purchase History");
        this.editPurchaseHistory = new Checkbox();
        HorizontalLayout textCheckboxLayout2 = new HorizontalLayout();
        textCheckboxLayout2.add(span2);
        textCheckboxLayout2.addAndExpand(new HorizontalLayout());
        textCheckboxLayout2.add(this.editPurchaseHistory);
        textWithCheckboxes.add(textCheckboxLayout2);
        this.assignRoleSection.add(textWithCheckboxes);

        Span span3 = new Span("Add or Edit Discount History");
        this.editDiscountHistory = new Checkbox();
        HorizontalLayout textCheckboxLayout3 = new HorizontalLayout();
        textCheckboxLayout3.add(span3);
        textCheckboxLayout3.addAndExpand(new HorizontalLayout());
        textCheckboxLayout3.add(this.editDiscountHistory);
        textWithCheckboxes.add(textCheckboxLayout3);
        this.assignRoleSection.add(textWithCheckboxes);

        HorizontalLayout assign_btn_layout = new HorizontalLayout();
        this.assign_btn = new Button("Assign");
        this.assign_btn.addClassName("manager-setting-btn");
        assign_btn_layout.add(this.assign_btn );
        assign_btn_layout.addClassName("manager-setting-btn-layout");

        this.update_btn = new Button("Update");
        this.update_btn.addClassName("manager-setting-btn");
        assign_btn_layout.add(this.update_btn);
        assign_btn_layout.addClassName("manager-setting-btn-layout");

        this.unassign_btn = new Button("Unassign");
        this.unassign_btn.addClassName("manager-setting-btn");
        assign_btn_layout.add(this.unassign_btn);
        assign_btn_layout.addClassName("manager-setting-btn-layout");

        this.assignRoleSection.add(assign_btn_layout);
    }

    public void createPermissionSection(String sectionTitle) {
        this.permissionSection.addClassName("manager-settings-permission-section");

        H2 title = new H2(sectionTitle);
        this.permissionSection.add(title);
        title.getStyle().set("padding", "20px");

        VerticalLayout textWithCheckboxes = new VerticalLayout();
        String[] texts = {"Edit products", "Add or Edit Purchase History", "Add or Edit Discount History"};
        int i = 0;
        for (String text : texts) {
            Span span = new Span(text);
            Checkbox checkbox = new Checkbox();
            checkbox.setValue(PermissionHandler.hasPermission(this.store_id, i));
            checkbox.setEnabled(false);
            HorizontalLayout textCheckboxLayout = new HorizontalLayout();
            textCheckboxLayout.add(span);
            textCheckboxLayout.addAndExpand(new HorizontalLayout());
            textCheckboxLayout.add(checkbox);
            textWithCheckboxes.add(textCheckboxLayout);
            i++;
        }

        HorizontalLayout resign_btn_layout = new HorizontalLayout();
        this.resign_btn = new Button("Resign");
        this.resign_btn.addClassName("manager-setting-btn");
        resign_btn_layout.add(this.resign_btn);
        resign_btn_layout.addClassName("manager-setting-btn-layout");

        this.permissionSection.add(textWithCheckboxes);

        if(PermissionHandler.getRole(store_id).equals("Owner")){
            this.permissionSection.add(resign_btn_layout);
        }
    }

    public void setAssignRoleClickEventListener(ComponentEventListener<ClickEvent<Button>> e){
        this.assign_btn.addClickListener(e);
    }

    public void setUpdateRoleClickEventListener(ComponentEventListener<ClickEvent<Button>> e){
        this.update_btn.addClickListener(e);
    }

    public void setUnassignRoleClickEventListener(ComponentEventListener<ClickEvent<Button>> e){
        this.unassign_btn.addClickListener(e);
    }

    public void setResignRoleClickEventListener(ComponentEventListener<ClickEvent<Button>> e){
        this.resign_btn.addClickListener(e);
    }

    public void setStore(StoreDTO store) {
        this.store = store;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        try {
            if (s != null && !s.isEmpty()) {
                this.store_id = Integer.parseInt(s);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + e.getMessage());
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String username = (String) VaadinSession.getCurrent().getAttribute("current-user");

        if (username == null) {
            event.rerouteTo("login");
        }else{
            this.presenter = new ManagerSettingPresenter(this);
        }
    }

    public int getStore_id() {
        return this.store_id;
    }

    public TextField getRoleField() {
        return roleField;
    }

    public Checkbox getIsOwner() {
        return isOwner;
    }

    public Checkbox getIsManager() {
        return isManager;
    }

    public Checkbox getEditProducts() {
        return editProducts;
    }

    public Checkbox getEditPurchaseHistory() {
        return editPurchaseHistory;
    }

    public Checkbox getEditDiscountHistory() {
        return editDiscountHistory;
    }

}
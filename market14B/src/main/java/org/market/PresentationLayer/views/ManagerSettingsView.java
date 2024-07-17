package org.market.PresentationLayer.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.handlers.PermissionHandler;
import org.market.PresentationLayer.presenter.ManagerSettingPresenter;
import org.market.PresentationLayer.views.components.PurchaseHistory;
import org.market.Web.DTOS.StoreDTO;

import java.util.ArrayList;
import java.util.List;

@Route(value = "settings", layout = HomeView.class)
@RoutePrefix("dash")
public class ManagerSettingsView extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {

    private VerticalLayout history_layout;
    private int store_id;
    private StoreDTO store;
    private HorizontalLayout topBar;
    private Div assignRoleSection;
    private Div permissionSection;
    private Div discountSection;
    private Div policySection;
    private ManagerSettingPresenter presenter;
    private TextField roleField;
    private TextField discountTitle;
    private TextField purchaseTitle;
    private NumberField discountPercentage;
    private IntegerField minPrice;
    private IntegerField minQuantity;
    private IntegerField quantity;
    private IntegerField price; //purchase policy
    private IntegerField weight; //purchase policy
    private IntegerField age; //purchase policy
    private DatePicker date; //purchase policy
    private Checkbox isOwner;
    private Checkbox isManager;
    private Checkbox editProducts;
    private Checkbox editPurchaseHistory;
    private Checkbox editDiscountHistory;
    private RadioButtonGroup<String> ppolicyType; // user/category/store/..
    private RadioButtonGroup<String> dpolicyType; // user/category/store/..
    private RadioButtonGroup<String> atLeast; //purchasePolicy max/min
    private RadioButtonGroup<String> policyOn; // quantity/age/price/..
    private Button assign_btn;
    private Button update_btn;
    private Button unassign_btn;
    private Button resign_btn;
    private Button delete_btn;
    private Button addDiscount_btn;
    private Button addPurchase_btn;
    private VerticalLayout currentDiscountsLayout;
    private VerticalLayout currentPoliciesLayout;
    private List<String> currentDiscountPolicies = new ArrayList<>();
    private List<String> currentPurchasePolicies = new ArrayList<>();
    private Select<String> selectpp;
    private Select<String> selectdp;
    private TextField categoryNamedp;
    private TextField categoryNamepp;
    private TextField productNamedp;
    private TextField productNamepp;

    public ManagerSettingsView() {
        addClassName("manager-settings-view");

        this.topBar = new HorizontalLayout();
        add(topBar);

        this.assignRoleSection = new Div();
        this.permissionSection = new Div();
        this.discountSection = new Div();
        this.policySection = new Div();

        HorizontalLayout mainContent = new HorizontalLayout(assignRoleSection, permissionSection);
        HorizontalLayout secondContent = new HorizontalLayout(discountSection, policySection);
        mainContent.addClassName("manager-settings-main-content");
        secondContent.addClassName("manager-settings-second-content");
        add(mainContent);

        this.history_layout = new VerticalLayout();
        add(history_layout);
        add(secondContent);
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


    public void createHistorySection() {
        this.history_layout.removeAll();
        Hr separator = new Hr();
        separator.addClassName("purchase-separator");
        PurchaseHistory history = new PurchaseHistory(store.getId());
        this.history_layout.add(separator);
        this.history_layout.add(history);

        Hr separator2 = new Hr();
        separator2.addClassName("purchase-separator");
        this.history_layout.add(separator2);
    }

    public void createDiscountSection(String sectionTitle) {

        this.discountSection.addClassName("manager-settings-discount-section");

        H2 title = new H2(sectionTitle);
        this.discountSection.add(title);
        title.getStyle().set("padding", "20px");

        currentDiscountsLayout = new VerticalLayout();
        updateCurrentDiscoutLayout();
        
        this.discountSection.add(currentDiscountsLayout);

        this.discountSection.add("add a new discount policy:");
        VerticalLayout discountFields = new VerticalLayout();

        selectdp = new Select<>();
        selectdp.setItems("none","AND", "OR","XOR", "IF_THEN");
        selectdp.setValue("none");
        discountFields.add(selectdp);
        this.discountTitle = new TextField("Discount Title");
        this.discountPercentage = new NumberField("Discount Percentage");
        this.discountPercentage.setMax(1);
        this.discountPercentage.setMin(0);

        this.dpolicyType = new RadioButtonGroup<>();
        dpolicyType.setLabel("Policy Type");
        dpolicyType.setItems("Product", "Category","Store");

        // TODO add selection options
        categoryNamedp = new TextField();
        productNamedp = new TextField();
        categoryNamedp.setPlaceholder("choose category");
        productNamedp.setPlaceholder("choose product");
        categoryNamedp.setVisible(false);
        productNamedp.setVisible(false);

        this.minPrice = new IntegerField("minimum price");
        minPrice.setValue(0);
        this.minQuantity = new IntegerField("minimum quantity");
        minQuantity.setValue(0);
        discountFields.add(discountTitle,discountPercentage,dpolicyType,categoryNamedp,productNamedp,minPrice,minQuantity);

        dpolicyType.addValueChangeListener(e-> {
            String selected = e.getValue();
            categoryNamedp.setVisible("Category".equals(selected));
            productNamedp.setVisible("Product".equals(selected));
        });
        this.discountSection.add(discountFields);

        addDiscount_btn = new Button("Add");
        this.discountSection.add(addDiscount_btn);
        //addDiscountClickEventListener(e -> addNewDPolicy(discountTitle.getValue()));
    }

//    public void addNewDPolicy(String policy) {
//        currentDiscountPolicies.add(policy);
//        updateCurrentDiscoutLayout();
//        clearDPolicyFields();
//        Notification.show("Discount Policy added");
//    }

    public void updateCurrentDiscoutLayout() {
        currentDiscountsLayout.removeAll();
        for (String policy : currentDiscountPolicies) {
            HorizontalLayout policyLayout = new HorizontalLayout();
            Span policySpan = new Span(policy);
            policyLayout.add(policySpan);
            policyLayout.addAndExpand(new HorizontalLayout());
            Icon deleteIcon = VaadinIcon.TRASH.create();
            delete_btn = new Button(deleteIcon);
            delete_btn.addClassName("manager-setting-btn");
            delete_btn.addClickListener(e -> {
                currentDiscountPolicies.remove(policy);
                updateCurrentDiscoutLayout();
            });
            policyLayout.setWidthFull();
            policyLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
            
            policyLayout.add(delete_btn);
            currentDiscountsLayout.add(policyLayout);
        }
    }

    public void clearDPolicyFields() {
        discountTitle.clear();
        discountPercentage.clear();
        minPrice.clear();
        minQuantity.clear();
    }


    public void createPolicySection(String sectionTitle) {
        this.policySection.addClassName("manager-settings-policy-section");

        H2 title = new H2(sectionTitle);
        this.policySection.add(title);
        title.getStyle().set("padding", "20px");
        
        currentPoliciesLayout = new VerticalLayout();
        updateCurrentPurchasePolicyLayout();
        
        this.policySection.add(currentPoliciesLayout);

        this.policySection.add("add a new purchase policy:");
        VerticalLayout purchaseFields = new VerticalLayout();

        selectpp = new Select<>();
        selectpp.setItems("none","AND", "OR","XOR", "IF_THEN");
        selectpp.setValue("none");
        purchaseFields.add(selectpp);
        purchaseTitle = new TextField("purchase policy title");
        this.date = new DatePicker("date");
        purchaseFields.add(purchaseTitle,date);
        this.ppolicyType = new RadioButtonGroup<>();
        ppolicyType.setLabel("Policy Type");
        ppolicyType.setItems("Product", "Category","User","Shopping Cart");
        
        // TODO add selection options
        categoryNamepp = new TextField();
        categoryNamepp.setPlaceholder("choose category");
        productNamepp = new TextField();
        productNamepp.setPlaceholder("choose product");
        categoryNamepp.setVisible(false);
        productNamepp.setVisible(false);
        
        this.atLeast = new RadioButtonGroup<>("", "min", "max");
        purchaseFields.add(ppolicyType,categoryNamepp,productNamepp, atLeast);
        
        policyOn = new RadioButtonGroup<>("", "Quantity", "Age", "Weight", "Price");
        purchaseFields.add(policyOn);
        
        this.quantity = new IntegerField("quantity");
        this.age = new IntegerField("age");
        this.weight = new IntegerField("weight");
        this.price = new IntegerField("price");
        purchaseFields.add(quantity, age, weight, price);
        quantity.setVisible(false);
        age.setVisible(false);
        weight.setVisible(false);
        price.setVisible(false);
        this.policySection.add(purchaseFields);

        policyOn.addValueChangeListener(e -> {
            String selected = e.getValue();
            quantity.setVisible("Quantity".equals(selected));
            age.setVisible("Age".equals(selected));
            weight.setVisible("Weight".equals(selected));
            price.setVisible("Price".equals(selected));
        });

        
        ppolicyType.addValueChangeListener(e-> {
            String selected = e.getValue();
            categoryNamepp.setVisible("Category".equals(selected));
            productNamepp.setVisible("Product".equals(selected));
        });

        addPurchase_btn = new Button("Add");
        this.policySection.add(addPurchase_btn);
//        addPurchaseClickEventListener(e -> addNewPPolicy(purchaseTitle.getValue()));

    }

//    public void addNewPPolicy(String policy) {
//        //currentPurchasePolicies.add(policy);
//        updateCurrentPurchasePolicyLayout();
//        clearPPolicyFields();
//        Notification.show("Purchase Policy added");
//    }

    public void updateCurrentPurchasePolicyLayout() {
        currentPoliciesLayout.removeAll();
        for (String policy : currentPurchasePolicies) {
            HorizontalLayout policyLayout = new HorizontalLayout();
            Span policySpan = new Span(policy);
            policyLayout.add(policySpan);
            policyLayout.addAndExpand(new HorizontalLayout());
            Icon deleteIcon = VaadinIcon.TRASH.create();
            delete_btn = new Button(deleteIcon);
            delete_btn.addClassName("manager-setting-btn");
            delete_btn.addClickListener(e -> {
                currentPurchasePolicies.remove(policy);
                updateCurrentPurchasePolicyLayout();
            });
            policyLayout.setWidthFull();
            policyLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

            policyLayout.add(delete_btn);
            currentPoliciesLayout.add(policyLayout);
        }
    }

    public void clearPPolicyFields() {
        date.clear();
        ppolicyType.clear();
        purchaseTitle.clear();
        atLeast.clear();
        quantity.clear();
        age.clear();
        weight.clear();
        date.clear();
        price.clear();
        policyOn.clear();

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

    public void setDeletePolicyClickEventListener(ComponentEventListener<ClickEvent<Button>> e) {
        this.delete_btn.addClickListener(e);
    }

    public void addDiscountClickEventListener(ComponentEventListener<ClickEvent<Button>> e) {
        this.addDiscount_btn.addClickListener(e);
    }

    public void addPurchaseClickEventListener(ComponentEventListener<ClickEvent<Button>> e) {
        this.addPurchase_btn.addClickListener(e);
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

    public NumberField getDiscountPercentage() {
        return discountPercentage;
    }

    public IntegerField getMinPrice() {
        return minPrice;
    }

    public IntegerField getMinQuantity() {
        return minQuantity;
    }

    public IntegerField getQuantity() {
        return quantity;
    }

    public IntegerField getPrice() {
        return price;
    }

    public IntegerField getWeight() {
        return weight;
    }

    public IntegerField getAge() {
        return age;
    }

    public DatePicker getDate() {
        return date;
    }

    public RadioButtonGroup<String> getPpolicyType() {
        return ppolicyType;
    }

    public RadioButtonGroup<String> getDpolicyType() {
        return dpolicyType;
    }

    public RadioButtonGroup<String> getAtLeast() {
        return atLeast;
    }

    public RadioButtonGroup<String> getPolicyOn() {
        return policyOn;
    }

    public Button getAssign_btn() {
        return assign_btn;
    }

    public Button getUpdate_btn() {
        return update_btn;
    }

    public Button getUnassign_btn() {
        return unassign_btn;
    }

    public Button getResign_btn() {
        return resign_btn;
    }

    public Button getDelete_btn() {
        return delete_btn;
    }

    public Button getAddDiscount_btn() {
        return addDiscount_btn;
    }

    public Button getAddPurchase_btn() {
        return addPurchase_btn;
    }

    public VerticalLayout getCurrentDiscountsLayout() {
        return currentDiscountsLayout;
    }

    public VerticalLayout getCurrentPoliciesLayout() {
        return currentPoliciesLayout;
    }

    public List<String> getCurrentDiscountPolicies() {
        return currentDiscountPolicies;
    }

    public List<String> getCurrentPurchasePolicies() {
        return currentPurchasePolicies;
    }

    public Select<String> getSelectpp() {
        return selectpp;
    }

    public Select<String> getSelectdp() {
        return selectdp;
    }

    public TextField getCategoryNamedp() {
        return categoryNamedp;
    }

    public TextField getCategoryNamepp() {
        return categoryNamepp;
    }

    public TextField getProductNamedp() {
        return productNamedp;
    }

    public TextField getProductNamepp() {
        return productNamepp;
    }

}

package org.market.PresentationLayer.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "settings", layout = HomeView.class)
@RoutePrefix("dash")
public class ManagerSettingsView extends VerticalLayout {

    public ManagerSettingsView() {
        addClassName("manager-settings-view");

        HorizontalLayout topBar = createTopBar();
        add(topBar);

        Div assignRoleSection = createAssignRoleSection("Assign Role");
        Div permissionSection = createPermissionSection("Permissions");

        HorizontalLayout mainContent = new HorizontalLayout(assignRoleSection, permissionSection);
        mainContent.addClassName("manager-settings-main-content");
        add(mainContent);
    }

    private HorizontalLayout createTopBar() {
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.addClassName("manager-settings-top-bar");
        topBar.setWidthFull();

        String username = VaadinSession.getCurrent().getAttribute("current-user").toString();
        HorizontalLayout nameLayout = createLabelWithValue("Name:", username);
        HorizontalLayout roleLayout = createLabelWithValue("Role:", "Manager");
        HorizontalLayout storeNameLayout = createLabelWithValue("Store Name:", "Electronic Store");

        topBar.add(nameLayout, roleLayout, storeNameLayout);
        return topBar;
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

    private Div createAssignRoleSection(String sectionTitle) {
        Div section = new Div();
        section.addClassName("manager-settings-role-section");

        H2 title = new H2(sectionTitle);
        section.add(title);
        title.getStyle().set("padding", "10px");

        TextField roleField = new TextField("Username");
        Checkbox checkBox1 = new Checkbox("Owner");
        Checkbox checkBox2 = new Checkbox("Manager");
        checkBox1.addValueChangeListener(e -> {
            if (e.getValue()) checkBox2.setValue(false);
        });
        checkBox2.addValueChangeListener(e -> {
            if (e.getValue()) checkBox1.setValue(false);
        });

        HorizontalLayout roleLayout = new HorizontalLayout(roleField, checkBox1, checkBox2);
        roleLayout.addClassName("manager-setting-role-layout");
        section.add(roleLayout);

        VerticalLayout textWithCheckboxes = new VerticalLayout();
        String[] texts = {"Edit products", "Add or Edit Purchase History", "Add or Edit Discount History"};
        for (String text : texts) {
            Span span = new Span(text);
            Checkbox checkbox = new Checkbox();
            HorizontalLayout textCheckboxLayout = new HorizontalLayout();
            textCheckboxLayout.add(span);
            textCheckboxLayout.addAndExpand(new HorizontalLayout());
            textCheckboxLayout.add(checkbox);
            textWithCheckboxes.add(textCheckboxLayout);
        }

        section.add(textWithCheckboxes);

        HorizontalLayout assign_btn_layout = new HorizontalLayout();
        Button assign = new Button("Assign");
        assign.addClassName("manager-setting-assign-btn");
        assign_btn_layout.add(assign);
        assign_btn_layout.addClassName("manager-setting-assign-btn-layout");

        section.add(assign_btn_layout);

        return section;
    }

    private Div createPermissionSection(String sectionTitle) {
        Div section = new Div();
        section.addClassName("manager-settings-permission-section");

        H2 title = new H2(sectionTitle);
        section.add(title);
        title.getStyle().set("padding", "20px");

        VerticalLayout textWithCheckboxes = new VerticalLayout();
        String[] texts = {"Edit products", "Add or Edit Purchase History", "Add or Edit Discount History"};
        for (String text : texts) {
            Span span = new Span(text);
            Checkbox checkbox = new Checkbox();
            checkbox.setValue(true);
            checkbox.setEnabled(false);
            HorizontalLayout textCheckboxLayout = new HorizontalLayout();
            textCheckboxLayout.add(span);
            textCheckboxLayout.addAndExpand(new HorizontalLayout());
            textCheckboxLayout.add(checkbox);
            textWithCheckboxes.add(textCheckboxLayout);
        }

        section.add(textWithCheckboxes);

        return section;
    }
}

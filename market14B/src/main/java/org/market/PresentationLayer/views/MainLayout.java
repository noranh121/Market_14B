package org.market.PresentationLayer.views;

import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import org.market.PresentationLayer.presenter.InitPresenter;
import org.market.PresentationLayer.views.components.UserMenu;
import org.market.Web.Requests.ReqUser;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 * <p>
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route
public class MainLayout extends AppLayout implements AfterNavigationObserver {
    private List<RouterLink> navLinks = new ArrayList<>();
    private InitPresenter presenter;

    public MainLayout() {
        Button send = new Button("send", e -> sendMessage());
        addToNavbar(send);
        addToNavbar(createTopBar());
        this.presenter = new InitPresenter(this);
    }

    private void sendMessage() {
        String loginUrl = "http://localhost:8080/api/users/send-message";

        RestTemplate restTemplate = new RestTemplate();
        ReqUser user = new ReqUser();
        user.setGuest("ssss");
        user.setPassword("ssss");
        user.setAge(20.4);
        user.setUsername("sassa");
        Boolean response = restTemplate.postForObject(loginUrl, user, Boolean.class);
    }

    private HorizontalLayout createTopBar() {
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setWidthFull();
        topBar.setPadding(true);
        topBar.setSpacing(true);
        topBar.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        // Title on the leftmost corner
        Span title = new Span("Market");
        title.getStyle().set("color", "Black").set("font-size", "50px").set("font-family","\"Times New Roman\", Times, serif");
        topBar.add(title);
        // Placeholder for navigation buttons
        HorizontalLayout nav = new HorizontalLayout();
        RouterLink homeLink = new RouterLink("Home", HomeView.class);
        RouterLink aboutLink = new RouterLink("About", AboutView.class);
        RouterLink contactLink = new RouterLink("contact", ContactView.class);

        homeLink.setClassName("customLink1");
        contactLink.setClassName("customLink1");
        aboutLink.setClassName("customLink1");

        homeLink.getElement().getClassList().add("active");

        nav.add(homeLink);
        nav.add(aboutLink);
        nav.add(contactLink);
        nav.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        nav.getStyle().set("margin-left", "50px");
        topBar.add(nav);

        nav.addClassName("nav-links");

        navLinks.add(homeLink);
        navLinks.add(aboutLink);
        navLinks.add(contactLink);


        // Spacer to push the sign up/sign in buttons to the rightmost corner
        topBar.addAndExpand(new HorizontalLayout());

        if(VaadinSession.getCurrent().getAttribute("current-user") == null) {
            // Sign up and sign in buttons
            RouterLink signUpLink = new RouterLink("sign up", SignupView.class);
            signUpLink.getStyle().set("margin-left", "auto");
            RouterLink loginLink = new RouterLink("login", LoginView.class);
            loginLink.getStyle().set("margin-left", "auto");

            signUpLink.setClassName("customLink2");
            signUpLink.getStyle().set("color", "#7368ed").set("border-color", "#7368ed");
            loginLink.setClassName("customLink2");

            topBar.add(signUpLink, loginLink);
        }
        else{
            UserMenu menu = new UserMenu((String) VaadinSession.getCurrent().getAttribute("current-user"));
            topBar.add(menu);
        }
        topBar.addClassName("nav-bar");
        return topBar;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        String route = event.getLocation().getPath();

        // Get the session attribute to check if this is a top-level navigation
        String topLevelRoute = (String) VaadinSession.getCurrent().getAttribute("topLevelRoute");
        System.out.println(topLevelRoute);

        if (route.contains("/")) {
            return;
        }

        // If the route has changed, update the session attribute and the navigation
        if (topLevelRoute == null || !topLevelRoute.equals(route)) {
            VaadinSession.getCurrent().setAttribute("topLevelRoute", route);

            navLinks.forEach(link -> {
                if (link.getHref().equals(route)) {
                    link.getElement().getClassList().add("active");
                } else {
                    link.getElement().getClassList().remove("active");
                }
            });
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        presenter.handleOnDetach();
    }
}

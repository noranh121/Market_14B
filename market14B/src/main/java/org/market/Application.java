package org.market;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import java.util.ArrayList;

import org.market.DataAccessLayer.DataController;
import org.market.DataAccessLayer.Repository.MarketRepository;
//import org.market.DomainLayer.backend.Market;
import java.util.List;

import org.market.DataAccessLayer.DataController;
import org.market.DataAccessLayer.Entity.Market;
import org.market.DataAccessLayer.Entity.User;
import org.market.DataAccessLayer.Repository.MarketRepository;
import org.market.ServiceLayer.ServiceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@PWA(name = "Project Base for Vaadin with Spring", shortName = "Project Base")
@Theme("my-theme")
@EnableAspectJAutoProxy
@EntityScan(basePackages = "org.market.DataAccessLayer.Entity")
@EnableJpaRepositories(basePackages = "org.market.DataAccessLayer.Repository")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        ServiceFactory serviceFactory = context.getBean(ServiceFactory.class);
        DataController dataController = context.getBean(DataController.class);
        dataController.clearAll();;
        try {
            serviceFactory.Register("admin", "password", 24);
            // serviceFactory.addToSystemManagers("admin");
            Market market = new Market();
            List<User> sysMngrs = new ArrayList<>();
            market.setOnline(false);
            market.setSystemManagers(sysMngrs);
            context.getBean(MarketRepository.class).save(market);
            serviceFactory.addToSystemManagers("admin");
            serviceFactory.init();
            serviceFactory.addCatagory(0, "Perfumes", "admin");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // ServiceFactory sf = ServiceFactory.getInstance();
        // sf.Register("u1","123",40);
        // sf.addToSystemManagers("u1");
        // sf.Register(u2,"123",20);
        // sf.Register(u3,"123",21);
        // sf.Register(u4,"123",22);
        // sf.Register(u5,"123",23);
        // sf.Register(u6,"123",24);
        // sf.Login(this,u2,123);
        // sf.initStore(u2,"s1");
        // sf.addCatagory(0,"snacks",u2);
        // sf.initProduct("u2","Bamba",0,"peanut-butter flavored","Osem",20);
        // sf.addProduct(1,0,30,20,"u2",20);
        // sf.AssignStoreManager(0,"u2","u3",new Boolean[]{true,false,false});
        // sf.AssignStoreOnwer(0,"u2","u4",new Boolean[]{true,false,false});
        // sf.AssignStoreOnwer(0,"u2","u5",new Boolean[]{true,false,false});
        //// sf. logout or resign ??
        // sf.logout("u1");
        // sf.resign(0,"u1");
    }

}

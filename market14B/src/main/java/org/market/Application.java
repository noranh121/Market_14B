package org.market;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
//@EnableAspectJAutoProxy
//@EntityScan(basePackages = "org.market.DataAccessLayer.Entity")
//@EnableJpaRepositories(basePackages = "org.market.DataAccessLayer.Repository")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
//        ServiceFactory sf = ServiceFactory.getInstance();
//        sf.Register("u1","123",40);
//        sf.addToSystemManagers("u1");
//        sf.Register(u2,"123",20);
//        sf.Register(u3,"123",21);
//        sf.Register(u4,"123",22);
//        sf.Register(u5,"123",23);
//        sf.Register(u6,"123",24);
//        sf.Login(this,u2,123);
//        sf.initStore(u2,"s1");
//        sf.addCatagory(0,"snacks",u2);
//        sf.initProduct("u2","Bamba",0,"peanut-butter flavored","Osem",20);
//        sf.addProduct(1,0,30,20,"u2",20);
//        sf.AssignStoreManager(0,"u2","u3",new Boolean[]{true,false,false});
//        sf.AssignStoreOnwer(0,"u2","u4",new Boolean[]{true,false,false});
//        sf.AssignStoreOnwer(0,"u2","u5",new Boolean[]{true,false,false});
//// sf. logout or resign ??
//        sf.logout("u1");
//        sf.resign(0,"u1");
    }

}

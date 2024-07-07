package org.market.DomainLayer.backend.UserPackage;

public class GuestUser extends User {

    public GuestUser(int Id,double age) {
        super(Integer.toString(Id),age);
        super.setLoggedIn(true);
    }

}

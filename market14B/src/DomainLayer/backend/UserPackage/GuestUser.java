package DomainLayer.backend.UserPackage;

import java.util.logging.Logger;

public class GuestUser extends User{

    private static final Logger LOGGER = Logger.getLogger(GuestUser.class.getName());
    private int Id;

    public GuestUser(int Id) {
        super(Integer.toString(Id));
        this.Id = Id;
    }

}

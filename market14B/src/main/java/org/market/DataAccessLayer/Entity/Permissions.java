package org.market.DataAccessLayer.Entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
//@Table(name="Permissions",catalog = "Market")
@Table(name="Permissions")
public class Permissions implements Serializable {
    
    @Id
    @JoinColumn(name="storeID",referencedColumnName = "storeID")
    private Store storeID;

    @ManyToOne
    @JoinColumn(name = "employer", referencedColumnName = "employer")
    private EmployerAndEmployeeEntity employer;

    public Store getStoreID() {
        return storeID;
    }

    public void setStoreID(Store storeID) {
        this.storeID = storeID;
    }

    public EmployerAndEmployeeEntity getEmployer() {
        return employer;
    }

    public void setEmployer(EmployerAndEmployeeEntity employer) {
        this.employer = employer;
    }
}

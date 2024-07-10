package org.market.DataAccessLayer.Entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// import javax.persistence.*;
import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EmployerAndEmployeeEntity implements Serializable{

    @Id
    @JoinColumn(name = "employer", referencedColumnName = "username")
    @Column(name = "employer")
    // @JoinColumn(name="username")
    private EmployerPermission employerUsername;

    public EmployerPermission getEmployerUsername() {
        return employerUsername;
    }

    public void setEmployerUsername(EmployerPermission employerUsername) {
        this.employerUsername = employerUsername;
    }

    public EmployerPermission findEmployee(String username){
        return employerUsername.findEmployee(username);
    }

    public void deleteEmployee(String username){
        if (employerUsername.getUsername().getUsername().equals(username)) {
            employerUsername = null; // If root matches, delete the whole tree
        }
        employerUsername.deleteEmployee(username);
    }

}

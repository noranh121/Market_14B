package DataAccessLayer.Entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class EmployerAndEmployeeEntity implements Serializable{

    @Id
    @JoinColumn(name = "employer", referencedColumnName = "username")
    private User employerUsername;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmployerPermission> employees;

    public User getEmployerUsername() {
        return employerUsername;
    }

    public void setEmployerUsername(User employerUsername) {
        this.employerUsername = employerUsername;
    }

    public List<EmployerPermission> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployerPermission> employees) {
        this.employees = employees;
    }

}

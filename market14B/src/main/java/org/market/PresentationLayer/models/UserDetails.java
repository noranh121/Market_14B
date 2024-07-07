package org.market.PresentationLayer.models;

import java.util.List;

public class UserDetails {

    private String username;
    private String password; // Ensure not to expose actual password in production
    private List<String> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    // Constructors, getters, setters
}


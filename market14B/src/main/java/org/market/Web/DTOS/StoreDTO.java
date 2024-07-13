package org.market.Web.DTOS;

import org.market.DomainLayer.backend.StorePackage.Store;

public class StoreDTO {
    private int id;
    private String name;
    private String description;
    private boolean isActive;
    private String firstOwner;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StoreDTO(Store store){
        this.id = store.getId();
        this.name = store.getName();
        this.description = store.getDescription();
        this.isActive = store.isActive();
        this.firstOwner = store.getFirstOwnerName();
    }

    public StoreDTO(){}

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

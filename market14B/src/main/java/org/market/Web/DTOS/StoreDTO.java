package org.market.Web.DTOS;

import org.market.DomainLayer.backend.StorePackage.Store;

public class StoreDTO {
    private int id;
    private String name;
    private String description;


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
        this.name = store.getFirstOwnerName() + "'s Store";
        this.description = store.getDescription();
    }

    public StoreDTO(){}

}

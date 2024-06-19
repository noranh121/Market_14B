package Web.DTOS;

import DomainLayer.backend.StorePackage.Store;

public class StoreDTO {
    private int id;
    private String name;
    private String description;

    public StoreDTO(Store store){
        this.id = store.getId();
        this.name = store.getFirstOwnerName() + "'s Store";
        this.description = store.getDescription();
    }

}

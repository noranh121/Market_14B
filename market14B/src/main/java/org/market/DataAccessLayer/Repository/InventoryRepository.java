package org.market.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.market.DataAccessLayer.Entity.Inventory;
import org.market.DataAccessLayer.Entity.Store;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Store>{

}

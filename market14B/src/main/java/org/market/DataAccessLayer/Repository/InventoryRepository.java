package org.market.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.market.DataAccessLayer.Entity.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Integer>{

}

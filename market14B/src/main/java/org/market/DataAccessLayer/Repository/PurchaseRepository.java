package org.market.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.market.DataAccessLayer.Entity.PurchasePolicy;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchasePolicy,Integer>{

}

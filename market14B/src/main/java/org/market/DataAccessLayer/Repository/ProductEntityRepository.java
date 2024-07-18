package org.market.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.market.DataAccessLayer.Entity.ProductEntityId;
import org.market.DataAccessLayer.Entity.ProductEntity;

@Repository
public interface ProductEntityRepository extends JpaRepository<ProductEntity,ProductEntityId>{
    
}

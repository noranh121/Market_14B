package org.market.DataAccessLayer.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.market.DataAccessLayer.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer>{
    
}

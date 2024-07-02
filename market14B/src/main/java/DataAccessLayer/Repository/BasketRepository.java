package DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import DataAccessLayer.Entity.Basket;
import DataAccessLayer.Entity.BasketId;

@Repository
public interface BasketRepository extends JpaRepository<Basket,BasketId>{

}

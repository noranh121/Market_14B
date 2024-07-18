package org.market.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.market.DataAccessLayer.Entity.Basket;
import org.market.DataAccessLayer.Entity.BasketId;

@Repository
public interface BasketRepository extends JpaRepository<Basket,BasketId>{

}

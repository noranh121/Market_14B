package org.market.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.market.DataAccessLayer.Entity.Market;

@Repository
public interface MarketRepository  extends JpaRepository<Market,Integer>{

}

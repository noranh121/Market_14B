package DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import DataAccessLayer.Entity.Market;

@Repository
public interface MarketRepository  extends JpaRepository<Market,Integer>{

}

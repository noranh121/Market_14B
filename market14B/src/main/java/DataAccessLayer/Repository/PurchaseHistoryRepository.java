package DataAccessLayer.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import DataAccessLayer.Entity.PurchaseHistory;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory,Integer>{

}

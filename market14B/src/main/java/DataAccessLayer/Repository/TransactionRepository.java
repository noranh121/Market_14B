package DataAccessLayer.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import DataAccessLayer.Entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer>{

}

package org.market.DataAccessLayer.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.market.DataAccessLayer.Entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer>{

}

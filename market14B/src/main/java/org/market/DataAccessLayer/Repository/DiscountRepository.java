package org.market.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.market.DataAccessLayer.Entity.DiscountPolicy;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountPolicy,Integer>{

}

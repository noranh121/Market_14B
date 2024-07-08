package org.market.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import org.market.DataAccessLayer.Entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>{

}

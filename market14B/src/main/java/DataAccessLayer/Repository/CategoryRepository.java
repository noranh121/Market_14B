package DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import DataAccessLayer.Entity.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer>{

}

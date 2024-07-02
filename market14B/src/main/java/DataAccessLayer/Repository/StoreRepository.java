package DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import DataAccessLayer.Entity.User;

public interface StoreRepository extends JpaRepository<User,Integer>{

}

package DataAccessLayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<User,Integer>{

}

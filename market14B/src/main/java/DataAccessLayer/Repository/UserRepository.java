package DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import DataAccessLayer.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,String>{

    


}

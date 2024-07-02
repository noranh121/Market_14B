package DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import DomainLayer.backend.Permission;

@Repository
public interface PermissionsRepository extends JpaRepository<Permission,Integer> {

}

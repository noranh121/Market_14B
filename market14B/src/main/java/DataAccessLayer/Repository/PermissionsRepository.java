package DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import DataAccessLayer.Entity.Permissions;

@Repository
public interface PermissionsRepository extends JpaRepository<Permissions,Integer> {

}

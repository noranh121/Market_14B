package DataAccessLayer.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import DataAccessLayer.Entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Integer> {

}

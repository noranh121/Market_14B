package org.market.DataAccessLayer.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.market.DataAccessLayer.Entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Integer> {

}

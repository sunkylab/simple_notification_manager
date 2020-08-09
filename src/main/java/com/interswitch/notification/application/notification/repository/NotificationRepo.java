package com.interswitch.notification.application.notification.repository;

import com.interswitch.notification.application.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {


    Notification findByRequestRef(String requestRef);
}

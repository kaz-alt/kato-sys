package com.workbench.kato_system.admin.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workbench.kato_system.admin.notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

}

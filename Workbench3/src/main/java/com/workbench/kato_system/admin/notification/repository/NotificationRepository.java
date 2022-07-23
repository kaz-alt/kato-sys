package com.workbench.kato_system.admin.notification.repository;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

  @Transactional
	@Modifying
  @Query("UPDATE Notification n SET n.checkTime = :checkTime WHERE n.employeeId = :employeeId")
  void updateCheckTime(@Param("checkTime") LocalDateTime checkTime, @Param("employeeId") Integer employeeId);

}

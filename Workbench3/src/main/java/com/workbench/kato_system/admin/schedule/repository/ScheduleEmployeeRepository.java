package com.workbench.kato_system.admin.schedule.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.schedule.model.ScheduleEmployee;

public interface ScheduleEmployeeRepository extends JpaRepository<ScheduleEmployee, Integer> {

  @Transactional
	@Modifying
  @Query("DELETE FROM ScheduleEmployee se WHERE se.scheduleId = :scheduleId")
  void deleteByScheduleId(@Param("scheduleId") Integer scheduleId);

}

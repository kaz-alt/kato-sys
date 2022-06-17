package com.workbench.kato_system.admin.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workbench.kato_system.admin.schedule.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

}

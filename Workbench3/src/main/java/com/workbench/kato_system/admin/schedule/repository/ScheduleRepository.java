package com.workbench.kato_system.admin.schedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.workbench.kato_system.admin.schedule.dto.ScheduleDto;
import com.workbench.kato_system.admin.schedule.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

  @Query("SELECT new com.workbench.kato_system.admin.schedule.dto.ScheduleDto("
    + "s.id, s.title, s.startTime, s.endTime, s.detail, s.isAllDay) FROM Schedule s")
  List<ScheduleDto> fetchDto();



}

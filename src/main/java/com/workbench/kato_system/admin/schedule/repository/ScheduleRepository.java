package com.workbench.kato_system.admin.schedule.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.schedule.dto.ScheduleDto;
import com.workbench.kato_system.admin.schedule.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

  @Query("SELECT new com.workbench.kato_system.admin.schedule.dto.ScheduleDto("
    + "s.id, s.title, s.startTime, s.endTime, s.detail, s.isAllDay) FROM Schedule s "
    + "WHERE s.startTime >= :start AND s.endTime < :end")
  List<ScheduleDto> fetchDto(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  @Query("SELECT s FROM Schedule s "
    + "LEFT JOIN FETCH s.scheduleEmployee se "
    + "LEFT JOIN FETCH se.employee "
    + "WHERE s.id = :id")
  Optional<Schedule> findByScheduleId(@Param("id") Integer id);

  @Query("SELECT DISTINCT s FROM Schedule s "
    + "LEFT JOIN FETCH s.scheduleEmployee se "
    + "LEFT JOIN FETCH se.employee e "
    + "WHERE s.startTime >= :today AND s.endTime < :tomorrow "
    + "AND se.employeeId = :employeeId "
    + "ORDER By s.startTime")
  List<Schedule> findByCurrentTime(
    @Param("today") LocalDateTime today,
    @Param("tomorrow") LocalDateTime tomorrow,
    @Param("employeeId") Integer employeeId);

    @Query("SELECT DISTINCT s FROM Schedule s "
    + "LEFT JOIN FETCH s.scheduleEmployee se "
    + "LEFT JOIN FETCH se.employee e "
    + "WHERE s.startTime >= :today AND s.endTime < :tomorrow "
    + "ORDER By s.startTime")
  List<Schedule> findEntireSchedule(
    @Param("today") LocalDateTime today, @Param("tomorrow") LocalDateTime tomorrow);

}

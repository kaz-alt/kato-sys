package com.workbench.kato_system.admin.timeline.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.timeline.model.Timeline;

public interface TimelineRepository extends JpaRepository<Timeline, Integer> {

  @Query(value = "SELECT t FROM Timeline t INNER JOIN FETCH t.employee LEFT JOIN FETCH t.timelineResponse tr LEFT JOIN FETCH tr.employee",
    countQuery = "SELECT COUNT(DISTINCT t) FROM Timeline t INNER JOIN t.employee LEFT JOIN t.timelineResponse tr LEFT JOIN tr.employee")
  Page<Timeline> fetchPage(Pageable pageable);

  @Query(value =
      "SELECT t FROM Timeline t "
    + "INNER JOIN FETCH t.employee "
    + "LEFT JOIN FETCH t.timelineResponse tr "
    + "LEFT JOIN FETCH tr.employee "
    + "WHERE t.id = :id")
  Timeline fetchOneById(@Param("id") Integer id);

  @Query(value =
      "SELECT * FROM timeline t "
    + "LEFT JOIN timeline_response r "
    + "ON t.id = r.timeline_id "
    + "WHERE t.created_date > ("
    +   "SELECT n.check_time FROM notification n "
    +   "WHERE n.employee_id = :employeeId ORDER BY n.check_time LIMIT 1) "
    + "OR (t.employee_id = :employeeId AND r.created_date > ("
    +   "SELECT n.check_time FROM notification n "
    +   "WHERE n.employee_id = :employeeId ORDER BY n.check_time LIMIT 1))", nativeQuery = true)
  List<Timeline> findByEmployeeIdAndCheckTime(@Param("employeeId") Integer employeeId);

  Timeline findFirstByOrderByCreatedDateDesc();

}

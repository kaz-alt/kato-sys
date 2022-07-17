package com.workbench.kato_system.admin.timeline.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.workbench.kato_system.admin.timeline.model.Timeline;

public interface TimelineRepository extends JpaRepository<Timeline, Integer> {

  @Query(value = "SELECT t FROM Timeline t INNER JOIN FETCH t.employee LEFT JOIN FETCH t.timelineResponse tr LEFT JOIN FETCH tr.employee",
    countQuery = "SELECT COUNT(DISTINCT t) FROM Timeline t INNER JOIN t.employee LEFT JOIN t.timelineResponse tr LEFT JOIN tr.employee")
  Page<Timeline> fetchPage(Pageable pageable);

}

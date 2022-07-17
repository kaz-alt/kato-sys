package com.workbench.kato_system.admin.timeline.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.workbench.kato_system.admin.timeline.model.Timeline;

public interface TimelineRepository extends JpaRepository<Timeline, Integer> {

  @Query("SELECT t FROM Timeline t LEFT JOIN FETCH t.timelineResponse tr")
  Page<Timeline> fetchPage(Pageable pageable);

}

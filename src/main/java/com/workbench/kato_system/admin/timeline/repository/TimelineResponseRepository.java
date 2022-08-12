package com.workbench.kato_system.admin.timeline.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workbench.kato_system.admin.timeline.model.TimelineResponse;

public interface TimelineResponseRepository extends JpaRepository<TimelineResponse, Integer> {

}

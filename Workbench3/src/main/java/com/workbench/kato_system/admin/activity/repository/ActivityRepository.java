package com.workbench.kato_system.admin.activity.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.activity.model.Activity;

public interface ActivityRepository
  extends JpaRepository<Activity, Integer>, JpaSpecificationExecutor<Activity> {

    @Query(value = "SELECT a FROM Activity a INNER JOIN FETCH a.staff LEFT JOIN FETCH a.project ORDER BY a.activityDate",
    countQuery = "SELECT COUNT(distinct a) FROM Activity a INNER JOIN a.staff LEFT JOIN a.project")
    Page<Activity> findPageList(Pageable pageable);

    @Query(value = "SELECT a FROM Activity a INNER JOIN FETCH a.staff LEFT JOIN FETCH a.project WHERE a.id = :id")
    Optional<Activity> findEntityById(@Param("id") Integer id);

}

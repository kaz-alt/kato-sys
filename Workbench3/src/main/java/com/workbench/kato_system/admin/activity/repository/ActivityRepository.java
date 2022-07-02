package com.workbench.kato_system.admin.activity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.workbench.kato_system.admin.activity.model.Activity;

public interface ActivityRepository
  extends JpaRepository<Activity, Integer>, JpaSpecificationExecutor<Activity> {

    @Query(value = "SELECT a FROM Activity a INNER JOIN FETCH a.staff",
    countQuery = "SELECT COUNT(distinct a) FROM Activity a INNER JOIN a.staff")
    Page<Activity> findPageList(Pageable pageable);

}

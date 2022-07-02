package com.workbench.kato_system.admin.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.workbench.kato_system.admin.activity.model.Activity;

public interface ActivityRepository
  extends JpaRepository<Activity, Integer>, JpaSpecificationExecutor<Activity> {

}

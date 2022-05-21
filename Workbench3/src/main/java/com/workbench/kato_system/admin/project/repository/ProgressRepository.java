package com.workbench.kato_system.admin.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workbench.kato_system.admin.project.model.Progress;

public interface ProgressRepository extends JpaRepository<Progress, Integer> {

}

package com.workbench.kato_system.admin.project.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.project.model.ProjectEmployee;

public interface ProjectEmployeeRepository extends JpaRepository<ProjectEmployee, Integer> {

	@Transactional
	@Modifying
	@Query("DELETE FROM ProjectEmployee pe WHERE pe.projectId = :projectId")
	void deleteByProjectId(@Param("projectId") Integer projectId);

}

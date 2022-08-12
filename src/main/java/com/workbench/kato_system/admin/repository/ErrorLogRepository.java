package com.workbench.kato_system.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workbench.kato_system.admin.model.ErrorLog;

public interface ErrorLogRepository extends JpaRepository<ErrorLog,Integer>{
}

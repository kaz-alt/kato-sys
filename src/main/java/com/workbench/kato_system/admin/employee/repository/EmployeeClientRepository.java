package com.workbench.kato_system.admin.employee.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.employee.model.EmployeeClient;

public interface EmployeeClientRepository extends JpaRepository<EmployeeClient, Integer> {

	@Override
	@Query(""
			+ "SELECT DISTINCT sc "
			+ "FROM EmployeeClient sc "
			+ "LEFT JOIN FETCH sc.employee s ")
	List<EmployeeClient> findAll();

	@Query(""
			+ "SELECT DISTINCT sc "
			+ "FROM EmployeeClient sc "
			+ "LEFT JOIN FETCH sc.employee s "
			+ "WHERE sc.clientId = :clientId")
	List<EmployeeClient> findByClientId(@Param("clientId") Integer clientId);

	@Transactional
	@Modifying
	@Query("DELETE FROM EmployeeClient sc WHERE sc.clientId = :clientId")
	void deleteByClientId(@Param("clientId") Integer clientId);

}

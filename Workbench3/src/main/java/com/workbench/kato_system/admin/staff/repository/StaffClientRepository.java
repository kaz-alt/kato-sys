package com.workbench.kato_system.admin.staff.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.staff.model.StaffClient;

public interface StaffClientRepository extends JpaRepository<StaffClient, Integer> {

	@Override
	@Query(""
			+ "SELECT DISTINCT sc "
			+ "FROM StaffClient sc "
			+ "LEFT JOIN FETCH sc.staff s ")
	List<StaffClient> findAll();

	@Query(""
			+ "SELECT DISTINCT sc "
			+ "FROM StaffClient sc "
			+ "LEFT JOIN FETCH sc.staff s "
			+ "WHERE sc.clientId = :clientId")
	List<StaffClient> findByClientId(@Param("clientId") Integer clientId);

	@Transactional
	@Modifying
	@Query("DELETE FROM StaffClient sc WHERE sc.clientId = :clientId")
	void deleteByClientId(@Param("clientId") Integer clientId);

}

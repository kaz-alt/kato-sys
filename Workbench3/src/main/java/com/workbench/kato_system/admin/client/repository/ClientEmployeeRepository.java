package com.workbench.kato_system.admin.client.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.client.model.entity.ClientEmployee;

public interface ClientEmployeeRepository extends JpaRepository<ClientEmployee, Integer> {

	@Override
	@Query("SELECT distinct cs FROM ClientEmployee cs LEFT JOIN FETCH cs.client")
	List<ClientEmployee> findAll();

	@Query("SELECT distinct cs FROM ClientEmployee cs LEFT JOIN FETCH cs.client WHERE cs.clientId = :clientId")
	List<ClientEmployee> findByClientId(@Param("clientId") Integer clientId);

	List<ClientEmployee> findOneByClientIdAndName(Integer clientId, String name);

	@Transactional
	@Modifying
	@Query("DELETE FROM ClientEmployee cs WHERE cs.clientId = :clientId")
	void deleteByClientId(@Param("clientId") Integer clientId);

}

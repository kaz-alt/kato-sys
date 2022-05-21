package com.workbench.kato_system.admin.client.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.client.model.entity.ClientStaff;

public interface ClientStaffRepository extends JpaRepository<ClientStaff, Integer> {

	@Override
	@Query("SELECT distinct cs FROM ClientStaff cs LEFT JOIN FETCH cs.client")
	List<ClientStaff> findAll();

	@Query("SELECT distinct cs FROM ClientStaff cs LEFT JOIN FETCH cs.client WHERE cs.clientId = :clientId")
	List<ClientStaff> findByClientId(@Param("clientId") Integer clientId);

	List<ClientStaff> findOneByClientIdAndName(Integer clientId, String name);

	@Transactional
	@Modifying
	@Query("DELETE FROM ClientStaff cs WHERE cs.clientId = :clientId")
	void deleteByClientId(@Param("clientId") Integer clientId);

}

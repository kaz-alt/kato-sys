package com.workbench.kato_system.admin.staff.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.staff.model.Staff;

public interface StaffRepository extends JpaRepository<Staff, Integer> {

	@Override
	@Query("select s from Staff s where s.delFlg = 0")
	Page<Staff> findAll(Pageable pageable);

	List<Staff> findByIdIn(List<Integer> idList);

	List<Staff> findByNameStartingWithOrNameKanaStartingWithAndDelFlgFalse(String name, String nameKana);
}

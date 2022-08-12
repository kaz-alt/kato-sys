package com.workbench.kato_system.admin.employee.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.workbench.kato_system.admin.employee.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

	@Override
	@Query("select e from Employee e where e.delFlg = 0")
	Page<Employee> findAll(Pageable pageable);

	List<Employee> findByIdIn(List<Integer> idList);

	Employee findByEmail(String email);

	Employee findByTel(String tel);

	List<Employee> findByNameStartingWithOrNameKanaStartingWithAndDelFlgFalse(String name, String nameKana);

}

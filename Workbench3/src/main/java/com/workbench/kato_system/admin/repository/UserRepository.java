package com.workbench.kato_system.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{

	@Query(value = "select u from User u "
			+ "where u.name = :name")
	User findByName(@Param("name") String name);
}

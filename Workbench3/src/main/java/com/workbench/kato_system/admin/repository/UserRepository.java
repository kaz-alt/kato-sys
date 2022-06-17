package com.workbench.kato_system.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workbench.kato_system.admin.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{

	User findByName(String name);
}

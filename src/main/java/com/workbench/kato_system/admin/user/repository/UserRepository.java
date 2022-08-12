package com.workbench.kato_system.admin.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workbench.kato_system.admin.user.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{

	User findByName(String name);

	User findByEmail(String email);
}

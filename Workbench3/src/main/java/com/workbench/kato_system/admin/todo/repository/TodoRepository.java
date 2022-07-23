package com.workbench.kato_system.admin.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.todo.model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Integer> {

  @Query(value =
      "SELECT t FROM Todo t "
    + "WHERE t.employeeId = :employeeId")
  List<Todo> findByEmployeeIdAndCreatedDate(@Param("employeeId") Integer employeeId);

}

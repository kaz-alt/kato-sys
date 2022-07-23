package com.workbench.kato_system.admin.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.workbench.kato_system.admin.login.model.LoginUserDetails;
import com.workbench.kato_system.admin.todo.form.ToDoForm;
import com.workbench.kato_system.admin.todo.model.Todo;
import com.workbench.kato_system.admin.todo.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {

	private final TodoRepository todoRepository;

	public List<Todo> getTodoList(Integer employeeId) {
		return todoRepository.findByEmployeeIdAndCreatedDate(employeeId);
	}

	public void save(ToDoForm form, LoginUserDetails user) {

		Todo t = new Todo();
		t.setEmployeeId(user.getUserId());
		t.setTask(form.getTask());
		t.setDeadline(form.getDeadline());
		t.setIsDone(false);

		todoRepository.save(t);

	}

	public void delete(Integer id) {

		Optional<Todo> todo = todoRepository.findById(id);

		if (todo.isPresent()) {
			Todo t = todo.get();
			todoRepository.delete(t);
		}
	}

}

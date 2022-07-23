package com.workbench.kato_system.admin.todo.form;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ToDoForm {

	@Size(max = 100, message = "100字以内で入力してください")
	private String task;

}

package com.workbench.kato_system.admin.todo.form;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ToDoForm {

	@Size(max = 100, message = "100字以内で入力してください")
	private String task;

	@NotNull(message = "期日を入力してください")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime deadline;

}

package com.workbench.kato_system.admin.client.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ClientEmployeeForm {

	private final String REQUIRED_MESSAGE = "※必須入力です";

	@NotEmpty(message = REQUIRED_MESSAGE)
	private String name;

	@NotEmpty(message = REQUIRED_MESSAGE)
	private String department;

	private String position;

	@NotEmpty(message = REQUIRED_MESSAGE)
	private String tel;

	@NotEmpty(message = REQUIRED_MESSAGE)
	private String email;

	@NotNull
	private Integer standpoint;

	@NotNull
	private Integer motivation;

	private String remarks;

}

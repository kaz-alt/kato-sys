package com.workbench.kato_system.admin.employee.form;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import com.workbench.kato_system.admin.user.form.UserForm;
import com.workbench.kato_system.admin.validation.ExistEmailValidation;

import lombok.Data;

@Data
public class EmployeeForm {

	public static interface UserFormGroup {}

	private final String REQUIRED_MESSAGE = "※必須入力です";
	private final String UNCORRECT_MESSAGE = "※入力が不正です";

	private Integer id;

	@NotEmpty(message=REQUIRED_MESSAGE)
	private String lastName;

	@NotEmpty(message=REQUIRED_MESSAGE)
	private String firstName;

	@NotEmpty(message=REQUIRED_MESSAGE)
	private String lastNameKana;

	@NotEmpty(message=REQUIRED_MESSAGE)
	private String firstNameKana;

	@NotEmpty(message=REQUIRED_MESSAGE)
	private String department;

	private String position;

	@NotEmpty(message=REQUIRED_MESSAGE)
	private String tel;

	@NotEmpty(message=REQUIRED_MESSAGE)
	@ExistEmailValidation
	private String email;

	private Integer joinYear;

	private Integer joinMonth;

	@Valid
	private UserForm userForm;

}

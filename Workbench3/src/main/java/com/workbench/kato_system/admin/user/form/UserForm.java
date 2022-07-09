package com.workbench.kato_system.admin.user.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.workbench.kato_system.admin.employee.form.EmployeeForm.UserFormGroup;

import lombok.Data;

@Data
public class UserForm {

	private final String REQUIRED_MESSAGE = "※必須入力です";
	private final String UNCORRECT_MESSAGE = "※5〜10文字以内で入力してください";
	private final String HANKAKU_MESSAGE = "※英数字で入力してください";

	private Integer id;

	@NotEmpty(message = REQUIRED_MESSAGE, groups= {UserFormGroup.class})
	@Size(min = 5, max = 10, groups= {UserFormGroup.class}, message = UNCORRECT_MESSAGE)
	@Pattern(regexp = "^[a-zA-Z0-9]+$", groups= {UserFormGroup.class}, message = HANKAKU_MESSAGE)
	private String password;

}

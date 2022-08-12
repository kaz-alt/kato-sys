package com.workbench.kato_system.admin.employee.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class ChangePasswordForm {

	private final String REQUIRED_MESSAGE = "※必須入力です";
	private final String UNCORRECT_MESSAGE = "※半角英数字記号で入力してください";

	@NotNull(message = REQUIRED_MESSAGE)
	private String email;

	@NotEmpty(message=REQUIRED_MESSAGE)
	@Pattern(regexp = "^[a-zA-Z0-9!-/:-@\\[-`{-~ ]*$", message = UNCORRECT_MESSAGE)
	private String password;

	@NotEmpty(message=REQUIRED_MESSAGE)
	@Pattern(regexp = "^[a-zA-Z0-9!-/:-@\\[-`{-~ ]*$", message = UNCORRECT_MESSAGE)
	private String confirmPassword;

}

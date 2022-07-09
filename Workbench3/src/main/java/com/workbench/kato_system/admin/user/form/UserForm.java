package com.workbench.kato_system.admin.user.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserForm {

	private final String REQUIRED_MESSAGE = "※必須入力です";
	private final String UNCORRECT_MESSAGE = "※パスワードは半角英数字5〜10文字以内で入力してください";

	private Integer id;

	@NotEmpty(message = REQUIRED_MESSAGE)
	private String name;

	@NotEmpty(message = REQUIRED_MESSAGE)
	private String email;

	@NotEmpty(message = REQUIRED_MESSAGE)
	@Size(min = 5, max = 10, message = UNCORRECT_MESSAGE)
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = UNCORRECT_MESSAGE)
	private String password;

}

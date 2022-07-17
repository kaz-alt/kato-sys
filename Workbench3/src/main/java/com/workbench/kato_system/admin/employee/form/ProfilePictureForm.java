package com.workbench.kato_system.admin.employee.form;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProfilePictureForm {

	private final String REQUIRED_MESSAGE = "※必須入力です";

	@NotNull(message=REQUIRED_MESSAGE)
	private Integer id;

	@NotNull
	private MultipartFile profilePicture;

}

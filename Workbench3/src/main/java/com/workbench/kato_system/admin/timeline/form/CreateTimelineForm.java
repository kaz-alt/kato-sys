package com.workbench.kato_system.admin.timeline.form;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateTimelineForm {

	private final String REQUIRED_MESSAGE = "※必須入力です";

	@NotEmpty(message=REQUIRED_MESSAGE)
	private String content;

	private MultipartFile image;

}

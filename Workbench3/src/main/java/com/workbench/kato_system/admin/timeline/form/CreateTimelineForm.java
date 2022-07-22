package com.workbench.kato_system.admin.timeline.form;

import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateTimelineForm {

	private Integer timelineId;

	@Size(max = 400, message = "400字以内で入力してください")
	private String content;

	private MultipartFile image;

}

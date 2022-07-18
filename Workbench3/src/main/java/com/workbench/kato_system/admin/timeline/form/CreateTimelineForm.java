package com.workbench.kato_system.admin.timeline.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateTimelineForm {

	private Integer timelineId;

	private String content;

	private MultipartFile image;

}

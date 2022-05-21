package com.workbench.kato_system.admin.inquiry_requirement_complaint.form;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class InquiryRequirementComplaintForm {

	private final String REQUIRED_MESSAGE = "※必須入力です";

	private Integer id;

	@NotNull(message = REQUIRED_MESSAGE)
	@Min(value = 1, message = REQUIRED_MESSAGE)
	private Integer clientId;

	@NotNull(message = REQUIRED_MESSAGE)
	private Integer contentType;

	@NotEmpty(message = REQUIRED_MESSAGE)
	private String content;

	@NotNull(message = REQUIRED_MESSAGE)
	@Min(value = 1, message = REQUIRED_MESSAGE)
	private Integer responsibleStaffId;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = REQUIRED_MESSAGE)
	private LocalDate occurredDate;

	private Boolean hasSolved;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate solvedDate;

	private String solvedContent;

}

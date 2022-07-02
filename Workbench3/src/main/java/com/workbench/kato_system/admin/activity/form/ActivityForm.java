package com.workbench.kato_system.admin.activity.form;

import java.time.LocalDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ActivityForm {

	private final String REQUIRED_MESSAGE = "※必須入力です";
	private final String UNCORRECT_MESSAGE = "※入力が不正です";

	private Integer id;

	@NotNull(message = REQUIRED_MESSAGE)
	@Min(value = 1, message = UNCORRECT_MESSAGE)
	private Integer staffId;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = REQUIRED_MESSAGE)
	private LocalDate activityDate;

	@NotEmpty(message = REQUIRED_MESSAGE)
	@Max(value = 2000, message = "2,000文字以内でご記入ください")
	private String content;

	private Integer projectId;

}

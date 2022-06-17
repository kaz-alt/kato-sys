package com.workbench.kato_system.admin.schedule.form;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ScheduleForm {

	private final String REQUIRED_MESSAGE = "※必須入力です";
	private final String UNCORRECT_MESSAGE = "※入力が不正です";

	private Integer id;

	private Integer staffId;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime endTime;

	@NotEmpty(message=REQUIRED_MESSAGE)
	private String title;

	private String detail;
}

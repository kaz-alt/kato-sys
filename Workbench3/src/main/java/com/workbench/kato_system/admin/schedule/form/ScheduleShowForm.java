package com.workbench.kato_system.admin.schedule.form;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ScheduleShowForm {

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime start;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime end;
}

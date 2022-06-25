package com.workbench.kato_system.admin.schedule.form;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ScheduleShowForm {

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime start;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime end;
}

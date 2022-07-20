package com.workbench.kato_system.admin.timeline.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimelineDto {

	private Integer timelineId;

	private LocalDateTime createdDate;

	private Integer employeeId;

	private LocalDateTime checkTime;

}

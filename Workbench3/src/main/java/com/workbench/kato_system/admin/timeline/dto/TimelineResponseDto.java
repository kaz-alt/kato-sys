package com.workbench.kato_system.admin.timeline.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimelineResponseDto {

	private Integer timelineId;

  private Integer employeeId;

	private String employeeName;

  private String content;

	private LocalDateTime createdDate;

	private byte[] image;

}

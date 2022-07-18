package com.workbench.kato_system.admin.timeline.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimelineDto {

	private Integer id;

  private Integer employeeId;

	private String employeeName;

  private String content;

	private LocalDateTime createdDate;

	private byte[] image;

  private TimelineResponseDto responses;

}

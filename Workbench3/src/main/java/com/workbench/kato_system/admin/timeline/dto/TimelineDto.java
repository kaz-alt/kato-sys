package com.workbench.kato_system.admin.timeline.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimelineDto {

	private Integer id;

	private LocalDateTime createdDate;

}

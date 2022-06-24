package com.workbench.kato_system.admin.schedule.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScheduleDto {

  @JsonProperty("id")
	private Integer id;

  @JsonProperty("title")
	private String title;

  @JsonProperty("start")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Tokyo")
	private LocalDateTime startTime;

  @JsonProperty("end")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Tokyo")
	private LocalDateTime endTime;

  @JsonProperty("description")
	private String detail;

  @JsonProperty("allDay")
	private Boolean allDay;

}

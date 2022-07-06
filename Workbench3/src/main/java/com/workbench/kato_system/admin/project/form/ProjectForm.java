package com.workbench.kato_system.admin.project.form;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ProjectForm {

	private final String REQUIRED_MESSAGE = "※必須入力です";
	private final String UNCORRECT_MESSAGE = "※入力が不正です";

	private Integer id;

	@NotEmpty(message = REQUIRED_MESSAGE)
	private String projectName;

	@NotNull(message = REQUIRED_MESSAGE)
	@Min(value = 1, message = REQUIRED_MESSAGE)
	private Integer clientId;

	private List<Integer> clientEmployeeIdList = new ArrayList<>();

	@NotNull(message = REQUIRED_MESSAGE)
	private Integer progress;

	@Min(value = 0)
	@Max(value = 100)
	private Integer orderProbability;

	@Min(value = 0)
	private Integer estimatedOrderAmount;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate expectedOrderDate;

	@NotNull(message = REQUIRED_MESSAGE)
	private Integer approachRoot;

	private String competitor;

	private Integer factor;
}

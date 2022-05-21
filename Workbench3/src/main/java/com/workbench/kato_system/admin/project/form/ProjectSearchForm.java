package com.workbench.kato_system.admin.project.form;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ProjectSearchForm {

	private List<Integer> clientIdList = new ArrayList<>();

	private String targetProjectName;

	private List<Integer> ourEmployeeIdList = new ArrayList<>();

	private List<Integer> progressIdList = new ArrayList<>();

	@Min(0)
	private Integer startOrderProbability;

	@Min(0)
	private Integer endOrderProbability;

	@Min(0)
	private Integer startEstimatedOrderAmount;

	@Min(0)
	private Integer endEstimatedOrderAmount;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startExpectedOrderDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endExpectedOrderDate;

	private List<Integer> approachRootIdList = new ArrayList<>();

	private String sortData = "clientId";

	private String sortOrder = "asc";

	private Integer pageNumber = 1;
}

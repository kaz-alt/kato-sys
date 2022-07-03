package com.workbench.kato_system.admin.activity.form;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ActivitySearchForm {

	private List<Integer> staffIdList = new ArrayList<>();

	private String targetContent;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startActivityDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endActivityDate;

	private List<Integer> projectIdList = new ArrayList<>();

	private String sortData = "activityDate";

	private String sortOrder = "asc";

	private Integer pageNumber = 1;
}

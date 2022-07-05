package com.workbench.kato_system.admin.inquiry_requirement_complaint.form;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class InquiryRequirementComplaintSearchForm {

	private List<Integer> clientIdList = new ArrayList<>();

	private Integer targetContentType;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startOccurredDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endOccurredDate;

	private Boolean targetHasSolved;

	private List<Integer> targetResponsibleEmployeeIdList = new ArrayList<>();

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startSolvedDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endSolvedDate;

	private String freeWord;

	private String sortData = "clientId";

	private String sortOrder = "asc";

	private Integer pageNumber = 1;
}

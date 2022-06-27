package com.workbench.kato_system.admin.staff.form;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class StaffSearchForm {

	private List<String> companyNameList = new ArrayList<>();

	private String targetName;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startExchangeDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endExchangeDate;

	private Integer pageNumber = 1;
}

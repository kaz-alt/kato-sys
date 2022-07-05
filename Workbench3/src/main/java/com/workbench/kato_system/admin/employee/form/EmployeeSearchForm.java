package com.workbench.kato_system.admin.employee.form;

import lombok.Data;

@Data
public class EmployeeSearchForm {

	private String targetName;

	private String targetDepartment;

	private String targetPosition;

	private Integer targetJoinYear;

	private Integer targetJoinMonth;

	private Integer condition;

	private Integer pageNumber = 1;
}

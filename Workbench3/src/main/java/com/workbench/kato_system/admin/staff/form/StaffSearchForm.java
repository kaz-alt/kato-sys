package com.workbench.kato_system.admin.staff.form;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class StaffSearchForm {

	private String targetName;

	private String targetDepartment;

	private String targetPosition;

	private Integer targetJoinYear;

	private Integer targetJoinMonth;

	private Integer condition;

	private Integer pageNumber = 1;
}

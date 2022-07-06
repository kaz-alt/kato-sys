package com.workbench.kato_system.admin.client.form;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ClientForm {

	private final String REQUIRED_MESSAGE = "※必須入力です";
	private final String UNCORRECT_MESSAGE = "※入力が不正です";

	private Integer id;

	@NotEmpty(message=REQUIRED_MESSAGE)
	private String clientName;

	private Integer industryId;

	private Integer clientTypeId;

	@NotEmpty(message=REQUIRED_MESSAGE)
	private String address;

	private String url;

	@Valid
	private List<ClientEmployeeForm> clientEmployee;

	@NotNull(message=REQUIRED_MESSAGE)
	private List<Integer> employeeIdList;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message=REQUIRED_MESSAGE)
	private LocalDate interviewDate;

}

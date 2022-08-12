package com.workbench.kato_system.admin.business_card.form;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BusinessCardForm {

	private final String REQUIRED_MESSAGE = "※必須入力です";
	private final String UNCORRECT_MESSAGE = "※入力が不正です";

	private Integer id;

	@NotEmpty(message = REQUIRED_MESSAGE)
	private String companyName;

	@NotEmpty(message = REQUIRED_MESSAGE)
	private String name;

	@NotEmpty(message = REQUIRED_MESSAGE)
	private String department;

	private String position;

	private String tel;

	private String email;

	private MultipartFile image;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate exchangeDate;
}

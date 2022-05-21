package com.workbench.kato_system.admin.product.form;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ProductForm {

	private final String REQUIRED_MESSAGE = "※必須入力です";
	private final String UNCORRECT_MESSAGE = "※入力が不正です";

	private Integer id;

	@NotEmpty(message = REQUIRED_MESSAGE)
	private String productName;

	@NotNull(message = REQUIRED_MESSAGE)
	@Min(value = 1, message = REQUIRED_MESSAGE)
	private Integer clientId;

	@NotNull(message = REQUIRED_MESSAGE)
	@Min(value = 0, message = UNCORRECT_MESSAGE)
	private Integer productQuantity;

	@NotNull(message = REQUIRED_MESSAGE)
	@Min(value = 0, message = UNCORRECT_MESSAGE)
	private Integer productSales;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = REQUIRED_MESSAGE)
	private LocalDate purchasedDate;

	private String productRemarks;

}

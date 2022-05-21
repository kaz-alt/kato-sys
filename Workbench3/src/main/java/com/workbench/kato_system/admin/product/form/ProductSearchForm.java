package com.workbench.kato_system.admin.product.form;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ProductSearchForm {

	private List<Integer> clientIdList = new ArrayList<>();

	private String targetProductName;

	@Min(0)
	private Integer startProductSales;

	@Min(0)
	private Integer endProductSales;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startPurchasedDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endPurchasedDate;

	private String sortData = "clientId";

	private String sortOrder = "asc";

	private Integer pageNumber = 1;
}

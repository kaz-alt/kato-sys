package com.workbench.kato_system.admin.product.dto;

import lombok.Data;

@Data
public class ProductDto {

	private String name;

	private Integer quantity;

	private Integer sales;

	private String purchasedDate;

	private String remarks;

}

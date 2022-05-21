package com.workbench.kato_system.admin.client.form;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ClientSearchForm {

	private List<String> targetClientNameList = new ArrayList<>();

	private List<Integer> industryIdList = new ArrayList<>();

	private List<Integer> clientTypeIdList = new ArrayList<>();

	private List<Integer> ourStaffIdList = new ArrayList<>();

	private String clientTel;

	private String clientEmail;

	private String sortData = "id";

	private String sortOrder = "asc";

	private Integer pageNumber = 1;
}

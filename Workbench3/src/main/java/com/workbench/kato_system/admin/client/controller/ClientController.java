package com.workbench.kato_system.admin.client.controller;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.workbench.kato_system.admin.client.dto.ClientDto;
import com.workbench.kato_system.admin.client.dto.ClientEmployeeDto;
import com.workbench.kato_system.admin.client.form.ClientForm;
import com.workbench.kato_system.admin.client.form.ClientSearchForm;
import com.workbench.kato_system.admin.client.model.entity.Client;
import com.workbench.kato_system.admin.client.model.enums.ClientType;
import com.workbench.kato_system.admin.client.model.enums.Industry;
import com.workbench.kato_system.admin.client.model.enums.Motivation;
import com.workbench.kato_system.admin.client.model.enums.Standpoint;
import com.workbench.kato_system.admin.client.service.ClientService;
import com.workbench.kato_system.admin.employee.service.EmployeeService;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.model.entity.InquiryRequirementComplaint;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.model.enums.ContentType;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.service.InquiryRequirementComplaintService;
import com.workbench.kato_system.admin.login.model.LoginUserDetails;
import com.workbench.kato_system.admin.product.form.ProductSearchForm;
import com.workbench.kato_system.admin.product.model.Product;
import com.workbench.kato_system.admin.product.service.ProductService;
import com.workbench.kato_system.admin.project.form.ProjectSearchForm;
import com.workbench.kato_system.admin.project.model.Project;
import com.workbench.kato_system.admin.project.service.ProjectService;
import com.workbench.kato_system.admin.utils.DateUtils;
import com.workbench.kato_system.admin.utils.PageNumberUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/client")
public class ClientController {

	private final int SIZE = 10;
	private final String REDIRECT = "redirect:/client";

	private final ClientService clientService;
	private final InquiryRequirementComplaintService inquiryRequirementComplaintService;
	private final ProductService productService;
	private final ProjectService projectService;
	private final EmployeeService employeeService;

	@ModelAttribute
	ClientForm setUpClientForm() {
		return new ClientForm();
	}

	@ModelAttribute
	ClientSearchForm setUpSearchClientForm() {
		return new ClientSearchForm();
	}

	@ModelAttribute
	ProductSearchForm setUpSearchProducttForm() {
		return new ProductSearchForm();
	}

	@ModelAttribute("industries")
	Industry[] setUpIndustry() {
		return Industry.values();
	}

	@ModelAttribute("types")
	ClientType[] setUpClientType() {
		return ClientType.values();
	}

	@ModelAttribute("standpoints")
	Standpoint[] setUpStandpoint() {
		return Standpoint.values();
	}

	@ModelAttribute("motivations")
	Motivation[] setUpMotivation() {
		return Motivation.values();
	}

	/**
	 * 一覧表示
	 */
	@GetMapping(value = "")
	public String index(Model model, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		Page<Client> page = clientService
				.getPageList(
						PageNumberUtils.getPageable(
								PageNumberUtils.revisePageNumber(pageNumber), SIZE, "id"));
		model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());
		model.addAttribute("projectCount", clientService.getProjectCount());

		return "client/index";
	}

	/**
	 * 条件検索
	 */
	@GetMapping(value = "/search")
	public String searchList(ClientSearchForm form, Model model,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		search(form, model, pageNumber);

		return "/client/index";
	}

	/**
	 * 新規登録
	 */
	@PostMapping(value = "/create")
	public String create(ClientForm form, BindingResult result,
			RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) {

		return save(form, result, attributes, user);
	}

	/**
	 * 編集
	 */
	@PostMapping(value = "/edit")
	public String edit(@Validated ClientForm form, BindingResult result,
			RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) {

		return save(form, result, attributes, user);
	}

	/**
	 * 削除
	 */
	@PostMapping(value = "/delete")
	public String delete(@RequestParam("clientId") Integer clientId) {

		clientService.delete(clientId);

		return REDIRECT;
	}

	/**
	 * 詳細ページ
	 */
	@GetMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id,
			@RequestParam(name = "year", required = false) Integer year) {

		Client client = clientService.getOne(id);

		LocalDate startDate = DateUtils.getStartFiscalDate(year);
		LocalDate endDate = DateUtils.getEndFiscalDate(year);

		Set<Project> projectList = projectService.getListByClientIdAndExpectedOrderDate(id, startDate, endDate);
		List<Product> list = productService.getListByClientIdAndPurchasedDate(id, startDate, endDate);
		List<InquiryRequirementComplaint> inquiryList = inquiryRequirementComplaintService
				.getByClientIdAndOccurredDate(id, startDate, endDate);

		model.addAttribute(client);
		model.addAttribute(projectList);
		model.addAttribute("list", list);
		model.addAttribute("inquiryData",
				inquiryList
						.stream()
						.filter(i -> i.getContentType() == ContentType.INQUIRY.getValue())
						.collect(Collectors.toList()));
		model.addAttribute("requirmentData",
				inquiryList
						.stream()
						.filter(i -> i.getContentType() == ContentType.REQUIREMNT.getValue())
						.collect(Collectors.toList()));
		model.addAttribute("complaintData",
				inquiryList
						.stream()
						.filter(i -> i.getContentType() == ContentType.COMPLAINT.getValue())
						.collect(Collectors.toList()));
		model.addAttribute("year", year == null ? startDate.getYear() : year);
		model.addAttribute("yearList", createYearList());
		model.addAttribute("projectSearchForm", new ProjectSearchForm());
		model.addAttribute("productSearch", new ProductSearchForm());

		return "client/detail";
	}

	/**
	 * 編集モーダル用
	 * fragmentを利用してHTMLを返す
	 */
	@GetMapping(value = "/show")
	public String show(Model model, @RequestParam("id") Integer id) {

		Client data = clientService.getOne(id);

		List<Integer> employeeClientIdList = data.getEmployeeClientList()
				.stream().map(s -> s.getEmployeeId()).collect(Collectors.toList());

		model.addAttribute("data", data);
		model.addAttribute("employeeClientIdList", employeeClientIdList);

		return "client/edit :: client-edit";
	}

	/**
	 * 顧客名を取得するAPI
	 */
	@RequestMapping("/api/get_client_name")
	@ResponseBody
	public List<String> getClient(@RequestParam(name = "name") String name) {
		if (StringUtils.isEmpty(name))
			return new ArrayList<>();
		return clientService.getClienNameListtByName(name);
	}

	/**
	 * 顧客名から顧客を取得するAPI
	 */
	@RequestMapping("api/get_client")
	@ResponseBody
	public List<ClientDto> getClients(@RequestParam(name = "name") String name) {
		if (StringUtils.isEmpty(name))
			return new ArrayList<>();
		List<ClientDto> dto = clientService.getClientDtoByName(name);
		return dto;
	}

	/**
	 * 顧客IDから顧客担当者を取得するAPI
	 */
	@RequestMapping("api/get_client_employee")
	@ResponseBody
	public List<ClientEmployeeDto> getClientEmployee(@RequestParam(name = "clientId") Integer clientId) {
		if (clientId == null) {
			return new ArrayList<ClientEmployeeDto>();
		}
		List<ClientEmployeeDto> dto = clientService.getClientEmployeeDtoByClientId(clientId);
		return dto;
	}

	/**
	 * 永続化処理
	 */
	private String save(ClientForm form, BindingResult result,
			RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) {

		if (result.hasErrors()) {

			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("clientForm", form);
			return REDIRECT;
		}

		clientService.save(form, user);

		return REDIRECT;
	}

	/**
	 * 検索処理
	 */
	private void search(ClientSearchForm form, Model model, Integer pageNumber) {

		Page<Client> page = clientService.getSearchResult(
				PageNumberUtils.revisePageNumber(pageNumber), form);
		model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());
		model.addAttribute("selectedEmployee",
				employeeService.getSelectedEmployee(form.getOurEmployeeIdList()));
		model.addAttribute("projectCount", clientService.getProjectCount());
	}

	private List<Integer> createYearList() {
		List<Integer> yearList = new ArrayList<>();
		int i = 0;
		while (i < 3) {
			int y = Year.now().getValue() - i;
			yearList.add(y);
			i++;
		}
		return yearList;
	}
}

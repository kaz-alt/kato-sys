package com.workbench.kato_system.admin.employee.controller;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.workbench.kato_system.admin.employee.dto.EmployeeDto;
import com.workbench.kato_system.admin.employee.form.EmployeeForm;
import com.workbench.kato_system.admin.employee.form.EmployeeSearchForm;
import com.workbench.kato_system.admin.employee.model.Employee;
import com.workbench.kato_system.admin.employee.service.EmployeeService;
import com.workbench.kato_system.admin.utils.PageNumberUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/employee")
public class EmployeeController {

	private final int SIZE = 30;
  private final String REDIRECT = "redirect:/employedd";

	private final EmployeeService employeeService;

	@ModelAttribute
	EmployeeForm setUpEmployeeForm() {
		return new EmployeeForm();
	}

	@ModelAttribute
	EmployeeSearchForm setUpEmployeeSearchForm() {
		return new EmployeeSearchForm();
	}

	/**
	 * 一覧表示
	 */
	@GetMapping(value = "")
	public String index(Model model, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		Page<Employee> page = employeeService.getPageList(PageNumberUtils.getPageable(pageNumber, SIZE, "id"));

    setUpModel(model, page);

		return "employee/index";
	}

  /**
	 * 条件検索
	 */
	@GetMapping(value = "/search")
	public String search(Model model, EmployeeSearchForm form) {

		Page<Employee> page = employeeService.getSearchResult(form);

    setUpModel(model, page);

		return "employee/index";
	}

	/**
	 * 新規登録
	 */
	@PostMapping(value = "/create")
	public String create(Model model, @Validated EmployeeForm form, BindingResult result, RedirectAttributes attributes) {

		return save(form, result, attributes);
	}

	/**
	 * 編集
	 */
	@PostMapping(value = "/edit")
	public String edit(Model model, @Validated EmployeeForm form, BindingResult result, RedirectAttributes attributes) {

		return save(form, result, attributes);
	}

	/**
	 * 削除
	 */
	@PostMapping(value = "/delete")
	public String delete(@RequestParam("employeeId") Integer employeeId) {

		employeeService.delete(employeeId);

		return REDIRECT;
	}

	/**
	 * 編集モーダル用
	 * fragmentを利用してHTMLを返す
	 */
	@GetMapping(value = "/show")
	public String show(Model model, @RequestParam("id") Integer id) {

		Employee data = employeeService.getOne(id);

		model.addAttribute("data", data);
		model.addAttribute("yearList", createYearList());
		model.addAttribute("monthList", createMonthList());

		return "employee/edit :: employee-edit";
	}

	/**
	 * 担当者を取得するAPI
	 */
	@GetMapping("/api/get_employee")
	@ResponseBody
	public List<EmployeeDto> getEmployee(@RequestParam("name") String name) {
		return employeeService.getEmployeeDtoByName(name);
	}

	/**
	 * 顧客IDから担当者を取得するAPI
	 */
	@GetMapping("/api/get_employee_by_client_id")
	@ResponseBody
	public List<EmployeeDto> getEmployeeClient(@RequestParam("clientId") Integer clientId) {
		return employeeService.getEmployeeDtoByClientId(clientId);
	}

  /**
	 * モデル共通処理
	 */
  private void setUpModel(Model model, Page<Employee> page) {
    model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());
		model.addAttribute("yearList", createYearList());
		model.addAttribute("monthList", createMonthList());
  }

	/**
	 * 永続化処理
	 */
	private String save(@Validated EmployeeForm form, BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {

			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("employeeForm", form);
			return REDIRECT;
		}

		employeeService.save(form);

		return REDIRECT;
	}

	/**
	 * 入社年作成
	 */
	private List<Integer> createYearList() {

		int startYear = 1980;

		YearMonth currentYearMonth = YearMonth.now();
		int currentYear = currentYearMonth.getYear();

		List<Integer> yearList = new ArrayList<>();

		while (startYear <= currentYear) {
			yearList.add(startYear);
			startYear++;
		}

		return yearList;

	}

	/**
	 * 入社月作成
	 */
	private List<Integer> createMonthList() {

		int startMonth = 1;

		List<Integer> monthList = new ArrayList<>();

		while (startMonth <= 12) {
			monthList.add(startMonth);
			startMonth++;
		}

		return monthList;
	}

}
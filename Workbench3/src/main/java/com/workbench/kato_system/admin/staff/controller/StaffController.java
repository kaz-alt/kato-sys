package com.workbench.kato_system.admin.staff.controller;

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

import com.workbench.kato_system.admin.staff.dto.EmployeeDto;
import com.workbench.kato_system.admin.staff.form.StaffForm;
import com.workbench.kato_system.admin.staff.form.StaffSearchForm;
import com.workbench.kato_system.admin.staff.model.Staff;
import com.workbench.kato_system.admin.staff.service.StaffService;
import com.workbench.kato_system.admin.utils.PageNumberUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/staff")
public class StaffController {

	private final int SIZE = 30;
  private final String REDIRECT = "redirect:/staff";

	private final StaffService staffService;

	@ModelAttribute
	StaffForm setUpStaffForm() {
		return new StaffForm();
	}

	@ModelAttribute
	StaffSearchForm setUpStaffSearchForm() {
		return new StaffSearchForm();
	}

	/**
	 * 一覧表示
	 */
	@GetMapping(value = "")
	public String index(Model model, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		Page<Staff> page = staffService.getPageList(PageNumberUtils.getPageable(pageNumber, SIZE, "id"));

    setUpModel(model, page);

		return "staff/index";
	}

  /**
	 * 条件検索
	 */
	@GetMapping(value = "/search")
	public String search(Model model, StaffSearchForm form) {

		Page<Staff> page = staffService.getSearchResult(form);

    setUpModel(model, page);

		return "staff/index";
	}

	/**
	 * 新規登録
	 */
	@PostMapping(value = "/create")
	public String create(Model model, @Validated StaffForm form, BindingResult result, RedirectAttributes attributes) {

		return save(model, form, result, attributes);
	}

	/**
	 * 編集
	 */
	@PostMapping(value = "/edit")
	public String edit(Model model, @Validated StaffForm form, BindingResult result, RedirectAttributes attributes) {

		return save(model, form, result, attributes);
	}

	/**
	 * 削除
	 */
	@PostMapping(value = "/delete")
	public String delete(@RequestParam("staffId") Integer staffId) {

		staffService.delete(staffId);

		return REDIRECT;
	}

	/**
	 * 編集モーダル用
	 * fragmentを利用してHTMLを返す
	 */
	@GetMapping(value = "/show")
	public String show(Model model, @RequestParam("id") Integer id) {

		Staff data = staffService.getOne(id);

		model.addAttribute("data", data);
		model.addAttribute("yearList", createYearList());
		model.addAttribute("monthList", createMonthList());

		return "staff/edit :: staff-edit";
	}

	/**
	 * 担当者を取得するAPI
	 */
	@GetMapping("/api/get_employee")
	@ResponseBody
	public List<EmployeeDto> getEmployee(@RequestParam("name") String name) {
		return staffService.getEmployeeDtoByName(name);
	}

	/**
	 * 顧客IDから担当者を取得するAPI
	 */
	@GetMapping("/api/get_employee_by_client_id")
	@ResponseBody
	public List<EmployeeDto> getEmployeeClient(@RequestParam("clientId") Integer clientId) {
		return staffService.getEmployeeDtoByClientId(clientId);
	}

  /**
	 * モデル共通処理
	 */
  private void setUpModel(Model model, Page<Staff> page) {
    model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());
		model.addAttribute("yearList", createYearList());
		model.addAttribute("monthList", createMonthList());
  }

	/**
	 * 永続化処理
	 */
	private String save(Model model, @Validated StaffForm form, BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {

			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("staffForm", form);
			return REDIRECT;
		}

		staffService.save(form);

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

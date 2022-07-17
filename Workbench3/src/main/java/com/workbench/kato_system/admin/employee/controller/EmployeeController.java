package com.workbench.kato_system.admin.employee.controller;

import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.workbench.kato_system.admin.employee.dto.EmployeeDto;
import com.workbench.kato_system.admin.employee.form.EmployeeForm;
import com.workbench.kato_system.admin.employee.form.EmployeeSearchForm;
import com.workbench.kato_system.admin.employee.form.ProfilePictureForm;
import com.workbench.kato_system.admin.employee.model.Employee;
import com.workbench.kato_system.admin.employee.service.EmployeeService;
import com.workbench.kato_system.admin.login.model.LoginUserDetails;
import com.workbench.kato_system.admin.utils.DateUtils;
import com.workbench.kato_system.admin.utils.PageNumberUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/employee")
public class EmployeeController {

	private final int SIZE = 30;
  private final String REDIRECT = "redirect:/employee";

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
	 * 詳細ページ
	 */
	@GetMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {

		Employee e = employeeService.getOne(id);
		model.addAttribute("employee", e);
		if (Objects.nonNull(e.getProfilePicture())) {
			model.addAttribute("picture", Base64.getEncoder().encodeToString(e.getProfilePicture()));
		}

		return "employee/detail";
	}

	/**
	 * 新規登録
	 */
	@PostMapping(value = "/create")
	public String create(Model model, @Validated EmployeeForm form, BindingResult result,
			RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) {

		return save(form, result, attributes, user);
	}

	/**
	 * 編集
	 */
	@PostMapping(value = "/edit")
	public String edit(Model model, @Validated EmployeeForm form, BindingResult result,
		RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) {

		return save(form, result, attributes, user);
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
		model.addAttribute("yearList", DateUtils.createYearList());
		model.addAttribute("monthList", DateUtils.createMonthList());

		return "employee/edit :: employee-edit";
	}

	/**
	 * プロフィール画像保存
	 */
	@PostMapping(value = "/setting_profile_picture")
	@ResponseBody
	public ResponseEntity<String> settingProfilePicture(@ModelAttribute @Validated ProfilePictureForm form, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>("fail to save", HttpStatus.BAD_REQUEST);
		}

		try {
			employeeService.saveProfilePicture(form);
		} catch (Exception e) {
			return new ResponseEntity<>("fail to save", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("success", HttpStatus.OK);
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
		model.addAttribute("yearList", DateUtils.createYearList());
		model.addAttribute("monthList", DateUtils.createMonthList());
		model.addAttribute("isUserForm", false);
  }

	/**
	 * 永続化処理
	 */
	private String save(@Validated EmployeeForm form, BindingResult result,
		RedirectAttributes attributes, LoginUserDetails user) {

		if (result.hasErrors()) {

			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("employeeForm", form);
			return REDIRECT;
		}

		employeeService.save(form, user);

		return REDIRECT;
	}

}

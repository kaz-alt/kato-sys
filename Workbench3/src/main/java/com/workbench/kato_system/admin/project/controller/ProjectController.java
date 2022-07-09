package com.workbench.kato_system.admin.project.controller;

import java.util.List;

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

import com.workbench.kato_system.admin.activity.form.ActivitySearchForm;
import com.workbench.kato_system.admin.client.service.ClientService;
import com.workbench.kato_system.admin.employee.service.EmployeeService;
import com.workbench.kato_system.admin.project.dto.ProjectDto;
import com.workbench.kato_system.admin.project.form.ProjectChangeProgressForm;
import com.workbench.kato_system.admin.project.form.ProjectForm;
import com.workbench.kato_system.admin.project.form.ProjectSearchForm;
import com.workbench.kato_system.admin.project.model.ApproachRoot;
import com.workbench.kato_system.admin.project.model.Factor;
import com.workbench.kato_system.admin.project.model.Progress;
import com.workbench.kato_system.admin.project.model.Project;
import com.workbench.kato_system.admin.project.service.ProjectService;
import com.workbench.kato_system.admin.security.LoginUserDetails;
import com.workbench.kato_system.admin.utils.PageNumberUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/project")
public class ProjectController {

	private final int SIZE = 10;
  private final String REDIRECT = "redirect:/project";

	private final ClientService clientService;
	private final ProjectService projectService;
	private final EmployeeService employeeService;

	@ModelAttribute
	ProjectForm setUpProjectForm() {
		return new ProjectForm();
	}

	@ModelAttribute
	ProjectSearchForm setUpProjectSearchForm() {
		return new ProjectSearchForm();
	}

	@ModelAttribute(name = "approachRoots")
	List<ApproachRoot> setUpApproachRoots() {
		return projectService.getAllApproachRoot();
	}

	@ModelAttribute(name = "factors")
	List<Factor> setUpFactors() {
		return projectService.getAllFactor();
	}

	@ModelAttribute(name = "progresses")
	List<Progress> setUpProgresses() {
		return projectService.getAllProgress();
	}

	/**
	 * 一覧表示
	 */
	@GetMapping(value = "")
	public String index(Model model, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		Page<Project> page = projectService.getPageList(
				PageNumberUtils.getPageable(
						PageNumberUtils.revisePageNumber(pageNumber), SIZE, "id"));
		model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());

		return "project/index";
	}

	/**
	 * 条件検索
	 */
	@GetMapping(value = "/search")
	public String search(Model model, @Validated ProjectSearchForm form,
			BindingResult result, RedirectAttributes attributes,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("projectSearchForm", form);
			attributes.addFlashAttribute("hasErrors", result.hasErrors());
			return REDIRECT;
		}

		Page<Project> page = projectService.getSearchResult(
				PageNumberUtils.revisePageNumber(pageNumber), form);
		model.addAttribute("page", page);
		model.addAttribute("projectList", page.getContent());
		model.addAttribute("selectedClient", clientService.getSelectedClient(form.getClientIdList()));
		model.addAttribute("selectedEmployee",
				employeeService.getSelectedEmployee(form.getOurEmployeeIdList()));

		return "project/index";
	}

	/**
	 * 新規登録
	 */
	@PostMapping(value = "/create")
	public String create(@Validated ProjectForm form, BindingResult result,
			RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) {

		return save(form, result, attributes, user);

	}

	/**
	 * 編集
	 */
	@PostMapping(value = "/edit")
	public String edit(@Validated ProjectForm form, BindingResult result,
			RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) {

		return save(form, result, attributes, user);
	}

	/**
	 * 削除
	 */
	@PostMapping(value = "/delete")
	public String delete(@RequestParam("projectId") Integer projectId) {

		projectService.delete(projectId);

		return REDIRECT;

	}

  /**
	 * 詳細表示
	 */
	@GetMapping(value = "/detail/{id}")
	public String detail(@PathVariable("id") Integer id, Model model) {

    Project data = projectService.getOne(id);

		model.addAttribute("data", data);
    model.addAttribute("list", data.getActivity());
    model.addAttribute("activitySearchForm", new ActivitySearchForm());

		return "project/detail";
	}

  /**
	 * 進捗変更
	 */
	@PostMapping(value = "/change_progress")
	public String changeProgress(ProjectChangeProgressForm form) {

		projectService.changeProgress(form);

    return REDIRECT;
	}

	/**
	 * 編集モーダル用
	 * fragmentを利用してHTMLを返す
	 */
	@GetMapping(value = "/show")
	public String show(Model model, @RequestParam("id") Integer id) {

		Project data = projectService.getOne(id);

		model.addAttribute("data", data);

		return "project/edit :: project-edit";
	}

	/**
	 * 永続化処理
	 */
	private String save(ProjectForm form, BindingResult result,
			RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) {

		if (result.hasErrors()) {

			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("projectForm", form);
			return REDIRECT;
		}

		projectService.save(form, user);

		return REDIRECT;
	}

  /**
	 * 担当者を取得するAPI
	 */
	@GetMapping("/api/get_project")
	@ResponseBody
	public List<ProjectDto> getEmployee(@RequestParam("name") String name) {
		return projectService.getProjectDtoByName(name);
	}
}

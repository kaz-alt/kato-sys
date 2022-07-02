package com.workbench.kato_system.admin.activity.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.workbench.kato_system.admin.activity.form.ActivityForm;
import com.workbench.kato_system.admin.activity.form.ActivitySearchForm;
import com.workbench.kato_system.admin.activity.model.Activity;
import com.workbench.kato_system.admin.activity.service.ActivityService;
import com.workbench.kato_system.admin.utils.PageNumberUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/activity")
public class ActivityController {

	private final int SIZE = 10;
  private final String REDIRECT = "redirect:/activity";

	private final ActivityService activityService;

	@ModelAttribute
	ActivityForm setUpActivityForm() {
		return new ActivityForm();
	}

	@ModelAttribute
	ActivitySearchForm setUpActivitySearchForm() {
		return new ActivitySearchForm();
	}

	/**
	 * 一覧表示
	 */
	@GetMapping(value = "")
	public String index(Model model, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		Page<Activity> page = activityService.getPageList(
				PageNumberUtils.getPageable(
						PageNumberUtils.revisePageNumber(pageNumber), SIZE, "id"));

		return list(page, model);
	}

	/**
	 * 条件検索
	 */
	@GetMapping(value = "/search")
	public String search(Model model, @Validated ActivitySearchForm form,
			BindingResult result, RedirectAttributes attributes,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("activitySearchForm", form);
			attributes.addFlashAttribute("hasErrors", result.hasErrors());
			return REDIRECT;
		}

		Page<Activity> page = activityService.getSearchResult(
				PageNumberUtils.revisePageNumber(pageNumber), form);
		return list(page, model);
	}

	/**
	 * 新規登録
	 */
	@PostMapping(value = "/create")
	public String create(@Validated ActivityForm form, BindingResult result,
			RedirectAttributes attributes) {

		return save(form, result, attributes);

	}

	/**
	 * 編集
	 */
	@PostMapping(value = "/edit")
	public String edit(@Validated ActivityForm form, BindingResult result,
			RedirectAttributes attributes) {

		return save(form, result, attributes);
	}

	/**
	 * 削除
	 */
	@PostMapping(value = "/delete")
	public String delete(@RequestParam("id") Integer id) {

		activityService.delete(id);

		return REDIRECT;

	}

	/**
	 * 編集モーダル用
	 * fragmentを利用してHTMLを返す
	 */
	@GetMapping(value = "/show")
	public String show(Model model, @RequestParam("id") Integer id) {

		Activity data = activityService.getOne(id);
		model.addAttribute("data", data);

		return "activity/edit :: activity-edit";
	}

	/**
	 * 一覧表示処理
	 */
	private String list(Page<Activity> page, Model model) {

		model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());

		return "activity/index";
	}

	/**
	 * 永続化処理
	 */
	private String save(ActivityForm form, BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {

			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("activityForm", form);
			return REDIRECT;
		}

		activityService.save(form);

		return REDIRECT;
	}
}

package com.workbench.kato_system.admin.inquiry_requirement_complaint.controller;

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

import com.workbench.kato_system.admin.client.service.ClientService;
import com.workbench.kato_system.admin.employee.service.EmployeeService;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.form.InquiryRequirementComplaintForm;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.form.InquiryRequirementComplaintSearchForm;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.model.entity.InquiryRequirementComplaint;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.model.enums.ContentType;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.service.InquiryRequirementComplaintService;
import com.workbench.kato_system.admin.utils.PageNumberUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/inquiry_requirement_complaint")
public class InquiryRequirementComplaintController {

	private final int SIZE = 10;
	private final String REDIRECT = "redirect:/inquiry_requirement_complaint";

	private final ClientService clientService;
	private final InquiryRequirementComplaintService service;
	private final EmployeeService employeeService;

	@ModelAttribute
	InquiryRequirementComplaintForm setUpInquiryRequirementComplaintForm() {
		return new InquiryRequirementComplaintForm();
	}

	@ModelAttribute
	InquiryRequirementComplaintSearchForm setUpInquiryRequirementComplaintSearchForm() {
		return new InquiryRequirementComplaintSearchForm();
	}

	@ModelAttribute("contentTypes")
	ContentType[] setUpContenType() {
		return ContentType.values();
	}

	/**
	 * 一覧表示
	 */
	@GetMapping(value = "")
	public String index(Model model, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		Page<InquiryRequirementComplaint> page = service
				.getPageList(PageNumberUtils.getPageable(
						PageNumberUtils.revisePageNumber(pageNumber), SIZE, "clientId"));
		model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());

		return "inquiry_requirement_complaint/index";
	}

	/**
	 * 条件検索
	 */
	@GetMapping(value = "/search")
	public String search(Model model, @Validated InquiryRequirementComplaintSearchForm form,
			BindingResult result, RedirectAttributes attributes,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("inquiryRequirementComplaintSearchForm", form);
			attributes.addFlashAttribute("hasErrors", result.hasErrors());
			return REDIRECT;
		}

		Page<InquiryRequirementComplaint> page = service.getSearchResult(
				PageNumberUtils.revisePageNumber(pageNumber), form);
		model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());
		model.addAttribute("selectedClient", clientService.getSelectedClient(form.getClientIdList()));
		model.addAttribute("selectedEmployee",
				employeeService.getSelectedEmployee(form.getTargetResponsibleEmployeeIdList()));

		return "inquiry_requirement_complaint/index";
	}

	/**
	 * 新規登録
	 */
	@PostMapping(value = "/create")
	public String create(@Validated InquiryRequirementComplaintForm form, BindingResult result,
			RedirectAttributes attributes) {

		return save(form, result, attributes);

	}

	/**
	 * 編集
	 */
	@PostMapping(value = "/edit")
	public String edit(@Validated InquiryRequirementComplaintForm form, BindingResult result,
			RedirectAttributes attributes) {

		return save(form, result, attributes);
	}

	/**
	 * 削除
	 */
	@PostMapping(value = "/delete")
	public String delete(@RequestParam("inquiryId") Integer inquiryId) {

		service.delete(inquiryId);

		return REDIRECT;
	}

	/**
	 * 編集モーダル用
	 * fragmentを利用してHTMLを返す
	 */
	@GetMapping(value = "/show")
	public String show(Model model, @RequestParam("id") Integer id) {

		InquiryRequirementComplaint data = service.getOne(id);

		model.addAttribute("data", data);

		return "inquiry_requirement_complaint/edit :: inquiry-edit";
	}

	/**
	 * 永続化処理
	 */
	private String save(InquiryRequirementComplaintForm form, BindingResult result,
			RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("inquiryRequirementComplaintForm", form);
			return REDIRECT;
		}
		service.save(form);

		return REDIRECT;
	}

}

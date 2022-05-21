package com.workbench.kato_system.admin.business_card.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.workbench.kato_system.admin.business_card.form.BusinessCardForm;
import com.workbench.kato_system.admin.business_card.form.BusinessCardSearchForm;
import com.workbench.kato_system.admin.business_card.model.BusinessCard;
import com.workbench.kato_system.admin.business_card.service.BusinessCardService;
import com.workbench.kato_system.admin.security.LoginUserDetails;
import com.workbench.kato_system.admin.utils.PageNumberUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/business_card")
public class BusinessCardController {

	private final int size = 10;

	private final BusinessCardService businessCardService;

	@ModelAttribute
	BusinessCardForm setUpBusinessCardForm() {
		return new BusinessCardForm();
	}

	@ModelAttribute
	BusinessCardSearchForm setUpBusinessCardSearchForm() {
		return new BusinessCardSearchForm();
	}

	/**
	 * 一覧表示
	 */
	@GetMapping(value = "")
	public String index(Model model, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		Page<BusinessCard> page = businessCardService.getPageList(
				PageNumberUtils.getPageable(
						PageNumberUtils.revisePageNumber(pageNumber), size, "id"));

		return list(page, model);
	}

	/**
	 * 条件検索
	 */
	@GetMapping(value = "/search")
	public String search(Model model, @Validated BusinessCardSearchForm form,
			BindingResult result, RedirectAttributes attributes,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("businessCardSearchForm", form);
			attributes.addFlashAttribute("hasErrors", result.hasErrors());
			return "redirect:/business_card";
		}

		Page<BusinessCard> page = businessCardService.getSearchResult(
				PageNumberUtils.revisePageNumber(pageNumber), form);
		return list(page, model);
	}

	/**
	 * 新規登録
	 * @throws IOException
	 */
	@PostMapping(value = "/create")
	public String create(@Validated BusinessCardForm form, BindingResult result,
			RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) throws IOException {

		return save(form, result, attributes, user);

	}

	/**
	 * 編集
	 * @throws IOException
	 */
	@PostMapping(value = "/edit")
	public String edit(@Validated BusinessCardForm form, BindingResult result,
			RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) throws IOException {

		return save(form, result, attributes, user);
	}

	/**
	 * 削除
	 */
	@PostMapping(value = "/delete")
	public String delete(@RequestParam("id") Integer id) {

		businessCardService.delete(id);

		return "redirect:/business_card";

	}

	/**
	 * 編集モーダル用
	 * fragmentを利用してHTMLを返す
	 */
	@GetMapping(value = "/show")
	public String show(Model model, @RequestParam("id") Integer id) {

		BusinessCard data = businessCardService.getOne(id);
		model.addAttribute("data", data);

		return "business_card/edit :: business-card-edit";
	}

	/**
	 * 画像ファイルをダウンロードする
	 */
	@GetMapping("/download_image")
	@ResponseBody
	public ResponseEntity<byte[]> downloadImage(@RequestParam("id") Integer id) {
		BusinessCard data = businessCardService.getOne(id);

		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", data.getContentType());
		header.setContentLength(data.getImage().length);
		try {
			header.add("Content-Disposition",
					"attachment; filename*=utf-8''" + URLEncoder.encode(data.getFileName(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<byte[]>(data.getImage(), header,
				HttpStatus.OK);
	}

	/**
	 * 一覧表示処理
	 */
	private String list(Page<BusinessCard> page, Model model) {

		List<String> images = new ArrayList<>();
		for (BusinessCard b : page.getContent()) {
			if (b.getImage() != null) {
				images.add(Base64.getEncoder().encodeToString(b.getImage()));
			} else {
				images.add(null);
			}
		}
		model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());
		model.addAttribute("images", images);

		return "business_card/index";
	}

	/**
	 * 永続化処理
	 * @throws IOException
	 */
	private String save(BusinessCardForm form, BindingResult result,
			RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) throws IOException {

		if (result.hasErrors()) {

			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("businessCardForm", form);
			return "redirect:/business_card";
		}

		businessCardService.save(form);

		return "redirect:/business_card";
	}
}

package com.workbench.kato_system.admin.product.controller;

import org.springframework.data.domain.Page;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.workbench.kato_system.admin.client.service.ClientService;
import com.workbench.kato_system.admin.product.form.ProductForm;
import com.workbench.kato_system.admin.product.form.ProductSearchForm;
import com.workbench.kato_system.admin.product.model.Product;
import com.workbench.kato_system.admin.product.service.ProductService;
import com.workbench.kato_system.admin.security.LoginUserDetails;
import com.workbench.kato_system.admin.utils.PageNumberUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/product")
public class ProductController {

	private final int SIZE = 10;
	private final String REDIRECT = "redirect:/product";

	private final ClientService clientService;
	private final ProductService productService;

	@ModelAttribute
	ProductForm setUpProductForm() {
		return new ProductForm();
	}

	@ModelAttribute
	ProductSearchForm setUpProductSearchForm() {
		return new ProductSearchForm();
	}

	/**
	 * 一覧表示
	 */
	@GetMapping(value = "")
	public String index(Model model, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		Page<Product> page = productService.getPageList(
				PageNumberUtils.getPageable(
						PageNumberUtils.revisePageNumber(pageNumber), SIZE, "clientId"));
		model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());

		return "product/index";
	}

	/**
	 * 条件検索
	 */
	@GetMapping(value = "/search")
	public String search(Model model, @Validated ProductSearchForm form,
			BindingResult result, RedirectAttributes attributes,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("productSearchForm", form);
			attributes.addFlashAttribute("hasErrors", result.hasErrors());
			return REDIRECT;
		}

		Page<Product> page = productService.getSearchResult(
				PageNumberUtils.revisePageNumber(pageNumber), form);
		model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());
		model.addAttribute("selectedClient", clientService.getSelectedClient(form.getClientIdList()));

		return "product/index";
	}

	/**
	 * 新規登録
	 */
	@PostMapping(value = "/create")
	public String create(@Validated ProductForm form, BindingResult result,
			RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) {

		return save(form, result, attributes, user);

	}

	/**
	 * 編集
	 */
	@PostMapping(value = "/edit")
	public String edit(@Validated ProductForm form, BindingResult result,
			RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) {

		return save(form, result, attributes, user);
	}

	/**
	 * 削除
	 */
	@PostMapping(value = "/delete")
	public String delete(@RequestParam("productId") Integer productId) {

		productService.delete(productId);

		return REDIRECT;

	}

	/**
	 * 編集モーダル用
	 * fragmentを利用してHTMLを返す
	 */
	@GetMapping(value = "/show")
	public String show(Model model, @RequestParam("id") Integer id) {

		Product data = productService.getOne(id);

		model.addAttribute("data", data);

		return "product/edit :: product-edit";
	}

	/**
	 * 永続化処理
	 */
	private String save(ProductForm form, BindingResult result,
			RedirectAttributes attributes, @AuthenticationPrincipal LoginUserDetails user) {

		if (result.hasErrors()) {

			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("productForm", form);
			return REDIRECT;
		}

		productService.save(form);

		return REDIRECT;
	}
}

package com.workbench.kato_system.admin.login.controller;

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

import com.workbench.kato_system.admin.user.form.UserForm;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/loginForm")
@RequiredArgsConstructor
public class LoginController {

	private final String REDIRECT = "redirect:/login/register";

	@ModelAttribute("userForm")
	public UserForm setUpUserForm() {
		return new UserForm();
	}


	@GetMapping("")
	public String index(Model model, @RequestParam(value = "error", required = false) boolean error){

		if(error) {
      model.addAttribute("hasError",true);
		}

		return "/login/loginForm";
	}

	@GetMapping("/register")
	public String register(Model model){

		return "/login/register";
	}

	@PostMapping("/register")
	public String registerUser(@Validated UserForm form, BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("userForm", form);
			return REDIRECT;
		}

		return REDIRECT;
	}

}

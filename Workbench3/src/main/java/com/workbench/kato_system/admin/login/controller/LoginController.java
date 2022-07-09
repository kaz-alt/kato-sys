package com.workbench.kato_system.admin.login.controller;

import javax.validation.groups.Default;

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

import com.workbench.kato_system.admin.employee.form.EmployeeForm;
import com.workbench.kato_system.admin.employee.form.EmployeeForm.UserFormGroup;
import com.workbench.kato_system.admin.user.form.UserForm;
import com.workbench.kato_system.admin.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/loginForm")
@RequiredArgsConstructor
public class LoginController {

	private final String REDIRECT = "redirect:/loginForm/register";

	private final UserService userService;

	@ModelAttribute("userForm")
	public UserForm setUpUserForm() {
		return new UserForm();
	}

	@ModelAttribute("employeeForm")
	public EmployeeForm setUpEmployeeForm() {
		return new EmployeeForm();
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

		model.addAttribute("isUserForm", true);

		return "/login/register2";
	}

	@PostMapping("/register")
	public String registerUser(Model model,
			@Validated({UserFormGroup.class, Default.class}) EmployeeForm form, BindingResult result,
			RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + result.getObjectName(), result);
			attributes.addFlashAttribute("userForm", form);
			attributes.addFlashAttribute("hasError", true);
			return REDIRECT;
		}

		userService.save(form);
		attributes.addFlashAttribute("isSuccess", true);

		return "redirect:/loginForm";
	}

}

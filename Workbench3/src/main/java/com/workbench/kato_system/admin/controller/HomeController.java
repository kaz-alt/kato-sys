package com.workbench.kato_system.admin.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.workbench.kato_system.admin.contact.form.ContactForm;
import com.workbench.kato_system.admin.contact.service.ContactService;
import com.workbench.kato_system.admin.login.model.LoginUserDetails;
import com.workbench.kato_system.admin.schedule.service.ScheduleService;
import com.workbench.kato_system.admin.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/")
public class HomeController {

	private final ContactService contactService;
	private final ScheduleService scheduleService;
	private final TodoService todService;

	@GetMapping(value = { "/", "/home" })
	String index(Model model, @AuthenticationPrincipal LoginUserDetails user) {

		model.addAttribute("scheduleList", scheduleService.getTodayScheduleList(user.getUserId()));
		model.addAttribute("todoList", todService.getTodoList(user.getUserId()));

		return "home/index";
	}

	/**
	 * 報告の追加
	 */
	@PostMapping(value = "/create_contact")
	@ResponseBody
	public ResponseEntity<String> create(
			@ModelAttribute @Validated ContactForm form,
			BindingResult result,
			@AuthenticationPrincipal LoginUserDetails user) {

		if (result.hasErrors()) {
			return new ResponseEntity<>("fail to save", HttpStatus.BAD_REQUEST);
		}

		try {
			contactService.save(form, user);
		} catch (Exception e) {
			return new ResponseEntity<>("an error has occurred while saving", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("success", HttpStatus.OK);
	}

}

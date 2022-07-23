package com.workbench.kato_system.admin.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.workbench.kato_system.admin.login.model.LoginUserDetails;
import com.workbench.kato_system.admin.schedule.service.ScheduleService;
import com.workbench.kato_system.admin.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/")
public class HomeController {

	private final ScheduleService scheduleService;
	private final TodoService todService;

	@GetMapping(value = { "/", "/home" })
	String index(Model model, @AuthenticationPrincipal LoginUserDetails user) {

		model.addAttribute("scheduleList", scheduleService.getTodayScheduleList());
		model.addAttribute("todoList", todService.getTodoList(user.getUserId()));

		return "home/index";
	}

}

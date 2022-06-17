package com.workbench.kato_system.admin.schedule.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.workbench.kato_system.admin.schedule.form.ScheduleForm;
import com.workbench.kato_system.admin.schedule.model.Schedule;
import com.workbench.kato_system.admin.schedule.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/schedule")
public class ScheduleController {

	private final ScheduleService scheduleService;

	@ModelAttribute
	ScheduleForm setUpScheduleForm() {
		return new ScheduleForm();
	}

	/**
	 * 一覧表示
	 */
	@GetMapping
	public String index(Model model) {

		List<Schedule> list = scheduleService.getAll();

		model.addAttribute("page", list);

		return "schedule/index";
	}

	/**
	 * スケジュール登録
	 */
	@PostMapping(value = "/create")
	public String create(@Validated ScheduleForm form, BindingResult result) {

		String text = "NG";

		if (result.hasErrors()) {
			return text;
		}

		try {

			scheduleService.save(form);

		} catch (Exception e) {

			text = "error occurred...";

		}

		return "redirect:/";
	}
}

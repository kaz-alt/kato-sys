package com.workbench.kato_system.admin.analysis.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.workbench.kato_system.admin.analysis.dto.AnalysisDto;
import com.workbench.kato_system.admin.project.service.ProjectService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/analysis")
public class AnalysisController {

	private final ProjectService projectService;

	/**
	 * 初期画面
	 */
	@GetMapping
	public String index() {

		return "analysis/index";
	}

	/**
	 * データ表示
	 */
  @ResponseBody
	@GetMapping("/get_data")
	public List<AnalysisDto> show(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		return projectService.getAnalysisDtoList();
	}

	/**
	 * fragmentを返す
	 */
	@GetMapping("/get_fragment")
	public String frag(Model model,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "date") String date) {

		model.addAttribute("previousDate", date);

		return "timeline/fragment :: timeline-fragment";
	}

}

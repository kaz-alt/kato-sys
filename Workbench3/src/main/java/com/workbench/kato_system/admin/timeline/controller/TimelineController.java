package com.workbench.kato_system.admin.timeline.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.workbench.kato_system.admin.timeline.model.Timeline;
import com.workbench.kato_system.admin.timeline.service.TimelineService;
import com.workbench.kato_system.admin.utils.PageNumberUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/timeline")
public class TimelineController {

	private final TimelineService timelineService;

	private final int SIZE = 20;
  private final String REDIRECT = "redirect:/timeline";

	/**
	 * 初期画面
	 */
	@GetMapping
	public String index(Model model, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		Page<Timeline> page = timelineService.getPageList(
			PageNumberUtils.getPageable(
				PageNumberUtils.revisePageNumber(pageNumber), SIZE, "id"));

		model.addAttribute("page", page);

		return "timeline/index";
	}

  /**
	 * 削除
	 */
	@PostMapping(value = "/delete")
	public String delete(@RequestParam("id") Integer id) {

		timelineService.delete(id);

		return REDIRECT;

	}

}

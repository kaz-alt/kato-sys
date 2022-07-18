package com.workbench.kato_system.admin.timeline.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

import com.workbench.kato_system.admin.login.model.LoginUserDetails;
import com.workbench.kato_system.admin.timeline.form.CreateTimelineForm;
import com.workbench.kato_system.admin.timeline.model.Timeline;
import com.workbench.kato_system.admin.timeline.service.TimelineService;
import com.workbench.kato_system.admin.utils.PageNumberUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/timeline")
public class TimelineController {

	private final TimelineService timelineService;

	private final int SIZE = 2;
  private final String REDIRECT = "redirect:/timeline";

	/**
	 * 初期画面
	 */
	@GetMapping
	public String index(Model model,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {

		setUpPage(model, pageNumber);

		return "timeline/index";
	}

	/**
	 * fragmentを返す
	 */
	@GetMapping("/get_fragment")
	public String frag(Model model,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "date") String date) {

		setUpPage(model, pageNumber);
		model.addAttribute("previousDate", date);

		return "timeline/fragment :: timeline-fragment";
	}

	/**
	 * 投稿
	 */
	@PostMapping(value = "/create")
	@ResponseBody
	public ResponseEntity<String> create(
			@ModelAttribute @Validated CreateTimelineForm form,
			BindingResult result,
			@AuthenticationPrincipal LoginUserDetails user) {

		if (result.hasErrors()) {
			return new ResponseEntity<>("fail to save", HttpStatus.BAD_REQUEST);
		}

		try {
			timelineService.save(form, user);
		} catch (Exception e) {
			return new ResponseEntity<>("an error has occurred while saving", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("success", HttpStatus.OK);
	}

	/**
	 * コメント
	 */
	@PostMapping(value = "/comment")
	@ResponseBody
	public ResponseEntity<String> comment(
			@ModelAttribute @Validated CreateTimelineForm form,
			BindingResult result,
			@AuthenticationPrincipal LoginUserDetails user) {

		if (result.hasErrors()) {
			return new ResponseEntity<>("fail to save", HttpStatus.BAD_REQUEST);
		}

		try {
			timelineService.saveComment(form, user);
		} catch (Exception e) {
			return new ResponseEntity<>("an error has occurred while saving", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("success", HttpStatus.OK);
	}

  /**
	 * 削除
	 */
	@PostMapping(value = "/delete")
	public String delete(@RequestParam("id") Integer id) {

		timelineService.delete(id);

		return REDIRECT;
	}

	private void setUpPage(Model model, int pageNumber) {

		Page<Timeline> page = timelineService.getPageList(
			PageRequest.of(
				PageNumberUtils.revisePageNumber(pageNumber), SIZE, Sort.by(Sort.Direction.DESC, "createdDate")));

		model.addAttribute("page", page);
	}

}

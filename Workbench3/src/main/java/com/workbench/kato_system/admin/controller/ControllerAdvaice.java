package com.workbench.kato_system.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.workbench.kato_system.admin.login.model.LoginUserDetails;
import com.workbench.kato_system.admin.notification.model.Notification;
import com.workbench.kato_system.admin.timeline.model.Timeline;
import com.workbench.kato_system.admin.timeline.service.TimelineService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvaice {

	private final TimelineService timelineService;

	@ModelAttribute
	void notification(Model model, @AuthenticationPrincipal LoginUserDetails user) {
		List<Timeline> list = timelineService.getLatestTimelines();
		List<Integer> uncheckeEmployeeId = new ArrayList<>();

		for (Timeline timeline : list) {
			for (Notification n : timeline.getNotification()) {
				if (!uncheckeEmployeeId.contains(n.getEmployeeId())) {
					uncheckeEmployeeId.add(n.getEmployeeId());
				}
			}
		}

		if (Objects.nonNull(user)) {
			model.addAttribute("isChecked", uncheckeEmployeeId.contains(user.getUserId()));
		}
	}

}

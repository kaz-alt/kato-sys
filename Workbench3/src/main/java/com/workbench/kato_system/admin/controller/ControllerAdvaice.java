package com.workbench.kato_system.admin.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.workbench.kato_system.admin.login.model.LoginUserDetails;
import com.workbench.kato_system.admin.timeline.model.Timeline;
import com.workbench.kato_system.admin.timeline.service.TimelineService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvaice {

	private final TimelineService timelineService;

	@ModelAttribute("isNotificationChecked")
	boolean notification(Model model, @AuthenticationPrincipal LoginUserDetails user) {

		if (Objects.nonNull(user)) {

			List<Timeline> tList = timelineService.checkLatestComment(user.getUserId());

			return !CollectionUtils.isEmpty(tList);

		}

		return true;
	}

}

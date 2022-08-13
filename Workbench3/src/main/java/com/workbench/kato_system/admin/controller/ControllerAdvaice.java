package com.workbench.kato_system.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.workbench.kato_system.admin.login.model.LoginUserDetails;
import com.workbench.kato_system.admin.schedule.model.Schedule;
import com.workbench.kato_system.admin.schedule.service.ScheduleService;
import com.workbench.kato_system.admin.timeline.model.Timeline;
import com.workbench.kato_system.admin.timeline.service.TimelineService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvaice {

	private final ScheduleService scheduleService;
	private final TimelineService timelineService;

	@ModelAttribute("isNotificationChecked")
	boolean notification(Model model, @AuthenticationPrincipal LoginUserDetails user) {

		if (Objects.nonNull(user)) {

			List<Timeline> tList = timelineService.checkLatestComment(user.getUserId());

			return !CollectionUtils.isEmpty(tList);

		}

		return true;
	}

	@ModelAttribute("sidebarScheduleList")
	List<Schedule> sidebarSchedule(Model model, @AuthenticationPrincipal LoginUserDetails user) {

		if (Objects.nonNull(user)) {

			return scheduleService.getEntireScheduleList();

		}

		return new ArrayList<>();
	}

}

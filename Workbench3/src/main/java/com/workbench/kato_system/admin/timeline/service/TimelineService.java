package com.workbench.kato_system.admin.timeline.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.workbench.kato_system.admin.login.model.LoginUserDetails;
import com.workbench.kato_system.admin.notification.service.NotificationService;
import com.workbench.kato_system.admin.timeline.form.CreateTimelineForm;
import com.workbench.kato_system.admin.timeline.model.Timeline;
import com.workbench.kato_system.admin.timeline.model.TimelineResponse;
import com.workbench.kato_system.admin.timeline.repository.TimelineRepository;
import com.workbench.kato_system.admin.timeline.repository.TimelineResponseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimelineService {

	private final TimelineRepository timelineRepository;
	private final TimelineResponseRepository timelineResponseRepository;
	private final NotificationService notificationService;

	public Page<Timeline> getPageList(Pageable pageable) {

		return timelineRepository.fetchPage(pageable);

	}

	public Timeline getOne(Integer id) {
		return timelineRepository.fetchOneById(id);
	}

	public void save(CreateTimelineForm form, LoginUserDetails user) throws IOException {

		Timeline t = new Timeline();
		t.setEmployeeId(user.getUserId());
		t.setContent(form.getContent());
		t.setCreatedDate(LocalDateTime.now());
		if (Objects.nonNull(form.getImage()) && StringUtils.hasText(form.getImage().getOriginalFilename())) {
			t.setImage(form.getImage().getBytes());
		}

		timelineRepository.save(t);

		notificationService.updateCheckTime(user.getUserId());
	}

	public void saveComment(CreateTimelineForm form, LoginUserDetails user) throws IOException {

		TimelineResponse t = new TimelineResponse();
		t.setTimelineId(form.getTimelineId());
		t.setEmployeeId(user.getUserId());
		t.setContent(form.getContent());
		t.setCreatedDate(LocalDateTime.now());
		if (Objects.nonNull(form.getImage()) && StringUtils.hasText(form.getImage().getOriginalFilename())) {
			t.setImage(form.getImage().getBytes());
		}

		timelineResponseRepository.save(t);

		notificationService.updateCheckTime(user.getUserId());
	}

	public void delete(Integer id) {

		Optional<Timeline> timeline = timelineRepository.findById(id);

		if (timeline.isPresent()) {
			Timeline t = timeline.get();
			timelineRepository.delete(t);
		}
	}

	public Timeline getLatestTimeline() {
		return timelineRepository.findFirstByOrderByCreatedDateDesc();
	}

}

package com.workbench.kato_system.admin.timeline.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.workbench.kato_system.admin.timeline.model.Timeline;
import com.workbench.kato_system.admin.timeline.repository.TimelineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimelineService {

	private final TimelineRepository timelineRepository;

	public Page<Timeline> getPageList(Pageable pageable) {

		return timelineRepository.fetchPage(pageable);

	}

	public void delete(Integer id) {

		Optional<Timeline> timeline = timelineRepository.findById(id);

		if (timeline.isPresent()) {
			Timeline t = timeline.get();
			timelineRepository.delete(t);
		}
	}

}

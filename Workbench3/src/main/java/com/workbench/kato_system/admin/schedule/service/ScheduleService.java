package com.workbench.kato_system.admin.schedule.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.workbench.kato_system.admin.schedule.form.ScheduleForm;
import com.workbench.kato_system.admin.schedule.model.Schedule;
import com.workbench.kato_system.admin.schedule.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;

	public List<Schedule> getAll() {
		return scheduleRepository.findAll();
	}

	public void save(ScheduleForm form) {

		Schedule schedule = new Schedule();
		schedule.setStaffId(1);
		schedule.setStartTime(LocalDateTime.now());
		schedule.setEndTime(LocalDateTime.now());
		schedule.setTitle(form.getTitle());
		schedule.setDetail("aaa");

		scheduleRepository.save(schedule);
	}
}

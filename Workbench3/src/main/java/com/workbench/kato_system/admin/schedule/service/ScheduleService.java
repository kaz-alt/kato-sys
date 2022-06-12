package com.workbench.kato_system.admin.schedule.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
}

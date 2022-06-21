package com.workbench.kato_system.admin.schedule.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.workbench.kato_system.admin.schedule.dto.ScheduleDto;
import com.workbench.kato_system.admin.schedule.form.ScheduleForm;
import com.workbench.kato_system.admin.schedule.model.Schedule;
import com.workbench.kato_system.admin.schedule.model.ScheduleEmployee;
import com.workbench.kato_system.admin.schedule.repository.ScheduleRepository;
import com.workbench.kato_system.admin.staff.model.Staff;
import com.workbench.kato_system.admin.staff.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;
	private final StaffRepository staffRepository;

	public List<ScheduleDto> getAll() {
		return scheduleRepository.fetchDto();
	}

	public Schedule getOne(Integer id) {
		Optional<Schedule> schedule = scheduleRepository.findById(id);
		return schedule.orElse(null);
	}

	public void save(ScheduleForm form) {

		Schedule schedule = new Schedule();
		schedule.setStartTime(form.getStartTime());
		schedule.setEndTime(form.getEndTime());
		schedule.setTitle(form.getTitle());
		schedule.setDetail(form.getDetail());
		schedule = scheduleRepository.save(schedule);

		Set<ScheduleEmployee> employeeSet = new HashSet<>();

		List<Staff> staffList = staffRepository.findByIdIn(form.getEmployeeIdList());

		for (Staff staff : staffList) {

			ScheduleEmployee se = new ScheduleEmployee();
			se.setEmployeeId(staff.getId());
			se.setScheduleId(schedule.getId());
			employeeSet.add(se);
		}

		schedule.setScheduleEmployee(employeeSet);

		scheduleRepository.save(schedule);
	}
}

package com.workbench.kato_system.admin.schedule.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.workbench.kato_system.admin.schedule.dto.ScheduleDto;
import com.workbench.kato_system.admin.schedule.form.ScheduleForm;
import com.workbench.kato_system.admin.schedule.model.Schedule;
import com.workbench.kato_system.admin.schedule.model.ScheduleEmployee;
import com.workbench.kato_system.admin.schedule.repository.ScheduleEmployeeRepository;
import com.workbench.kato_system.admin.schedule.repository.ScheduleRepository;
import com.workbench.kato_system.admin.staff.model.Staff;
import com.workbench.kato_system.admin.staff.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;
	private final ScheduleEmployeeRepository scheduleEmployeeRepository;
	private final StaffRepository staffRepository;

	public List<ScheduleDto> getSchedules(LocalDateTime start, LocalDateTime end) {
		return scheduleRepository.fetchDto(start, end);
	}

	public Schedule getOne(Integer id) {
		Optional<Schedule> schedule = scheduleRepository.findById(id);
		return schedule.orElse(null);
	}

	public void save(ScheduleForm form) {

		Schedule schedule = new Schedule();

		if (form.getId() != null) {
			schedule = scheduleRepository.findById(form.getId()).orElse(new Schedule());
			scheduleEmployeeRepository.deleteByScheduleId(schedule.getId());
		}

		schedule.setStartTime(form.getStartTime());
		schedule.setEndTime(form.getEndTime());
		schedule.setTitle(form.getTitle());
		schedule.setDetail(form.getDetail());
		schedule.setPlace(form.getPlace());
		schedule.setIsAllDay(form.getIsAllDay());
		schedule = scheduleRepository.save(schedule);

    if (!CollectionUtils.isEmpty(form.getEmployeeIdList())) {

      List<ScheduleEmployee> employeeList = new ArrayList<>();

      List<Staff> staffList = staffRepository.findByIdIn(form.getEmployeeIdList());

      for (Staff staff : staffList) {

        ScheduleEmployee se = new ScheduleEmployee();
        se.setStaffId(staff.getId());
        se.setScheduleId(schedule.getId());
        se.setStaff(staff);
        employeeList.add(se);
      }

      schedule.setScheduleEmployee(employeeList);

      scheduleRepository.save(schedule);
    }
	}

	public void delete(Integer id) {

		Optional<Schedule> schedule = scheduleRepository.findById(id);

		if (schedule.isPresent()) {
			Schedule s = schedule.get();
			scheduleRepository.delete(s);
		}
	}
}

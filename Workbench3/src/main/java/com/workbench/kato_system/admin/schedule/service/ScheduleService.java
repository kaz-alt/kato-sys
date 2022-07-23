package com.workbench.kato_system.admin.schedule.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.workbench.kato_system.admin.employee.model.Employee;
import com.workbench.kato_system.admin.employee.repository.EmployeeRepository;
import com.workbench.kato_system.admin.schedule.dto.ScheduleDto;
import com.workbench.kato_system.admin.schedule.form.ScheduleForm;
import com.workbench.kato_system.admin.schedule.model.Schedule;
import com.workbench.kato_system.admin.schedule.model.ScheduleEmployee;
import com.workbench.kato_system.admin.schedule.repository.ScheduleEmployeeRepository;
import com.workbench.kato_system.admin.schedule.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;
	private final ScheduleEmployeeRepository scheduleEmployeeRepository;
	private final EmployeeRepository employeeRepository;

	public List<ScheduleDto> getSchedules(LocalDateTime start, LocalDateTime end) {
		return scheduleRepository.fetchDto(start, end);
	}

	public Schedule getOne(Integer id) {
		Optional<Schedule> schedule = scheduleRepository.findByScheduleId(id);
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

      List<ScheduleEmployee> scheduleEmployeeList = new ArrayList<>();

      List<Employee> employeeList = employeeRepository.findByIdIn(form.getEmployeeIdList());

      for (Employee employee : employeeList) {

        ScheduleEmployee se = new ScheduleEmployee();
        se.setEmployeeId(employee.getId());
        se.setScheduleId(schedule.getId());
        se.setEmployee(employee);
        scheduleEmployeeList.add(se);
      }

      schedule.setScheduleEmployee(scheduleEmployeeList);

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

	public List<Schedule> getTodayScheduleList() {

		LocalDate now = LocalDate.now();
		LocalDateTime today = now.atStartOfDay();
		LocalDateTime tomorrow = now.plusDays(1).atStartOfDay();

		return scheduleRepository.findByCurrentTime(today, tomorrow);
	}
}

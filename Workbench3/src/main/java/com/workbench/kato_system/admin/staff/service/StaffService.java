package com.workbench.kato_system.admin.staff.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.workbench.kato_system.admin.staff.dto.EmployeeDto;
import com.workbench.kato_system.admin.staff.form.StaffForm;
import com.workbench.kato_system.admin.staff.model.Staff;
import com.workbench.kato_system.admin.staff.model.StaffClient;
import com.workbench.kato_system.admin.staff.repository.StaffClientRepository;
import com.workbench.kato_system.admin.staff.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffService {

	private final StaffRepository staffRepository;
	private final StaffClientRepository staffClientRepository;

	public List<Staff> getAll() {
		return staffRepository.findAll();
	}

	public List<EmployeeDto> getEmployeeDtoByName(String name) {
		List<Staff> staffList = staffRepository.findByName(name);
		return model2Dto(staffList);
	}

	public List<EmployeeDto> getEmployeeDtoByClientId(Integer clientId) {
		List<StaffClient> staffClientList = staffClientRepository.findByClientId(clientId);
		return staffClient2Dto(staffClientList);
	}

	public List<EmployeeDto> getSelectedEmployee(List<Integer> idList) {
		List<Staff> staffList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(idList)) {
			staffList = staffRepository.findByIdIn(idList);
		}
		return model2Dto(staffList);
	}

	public Page<Staff> getPageList(Pageable pageable) {
		if (pageable == null) {
			return null;
		}
		return staffRepository.findAll(pageable);
	}

	public Staff getOne(Integer id) {
		return staffRepository.findById(id).orElse(new Staff());
	}

	public Staff save(StaffForm form) {

		Staff staff = new Staff();

		staff.setId(form.getId() == null ? null : form.getId());

		staff.setName(form.getName());
		staff.setDepartment(form.getDepartment());
		staff.setPosition(form.getPosition());
		staff.setTel(form.getTel());
		staff.setEmail(form.getEmail());
		staff.setJoinYear(form.getJoinYear());
		staff.setJoinMonth(form.getJoinMonth());
		staff.setDelFlg(false);

		staff = staffRepository.save(staff);

		return staff;
	}

	public void delete(Integer id) {

		Optional<Staff> staff = staffRepository.findById(id);
		if (staff.isPresent()) {
			Staff s = staff.get();
			s.setDelFlg(true);
			staffRepository.save(s);
		}
	}

	private List<EmployeeDto> model2Dto(List<Staff> staffList) {

		List<EmployeeDto> dtoList = new ArrayList<>();

		if (CollectionUtils.isEmpty(staffList))
			return dtoList;

		for (Staff staff : staffList) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(staff.getId());
			dto.setName(staff.getName());
			dtoList.add(dto);
		}

		return dtoList;
	}

	private List<EmployeeDto> staffClient2Dto(List<StaffClient> staffClientList) {

		List<EmployeeDto> dtoList = new ArrayList<>();

		if (CollectionUtils.isEmpty(staffClientList))
			return dtoList;

		for (StaffClient sc : staffClientList) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(sc.getStaff().getId());
			dto.setName(sc.getStaff().getName());
			dtoList.add(dto);
		}

		return dtoList;
	}

}

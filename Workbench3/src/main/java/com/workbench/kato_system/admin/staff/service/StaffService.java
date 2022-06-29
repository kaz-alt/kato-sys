package com.workbench.kato_system.admin.staff.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.workbench.kato_system.admin.staff.dto.EmployeeDto;
import com.workbench.kato_system.admin.staff.form.StaffForm;
import com.workbench.kato_system.admin.staff.form.StaffSearchForm;
import com.workbench.kato_system.admin.staff.model.Staff;
import com.workbench.kato_system.admin.staff.model.StaffClient;
import com.workbench.kato_system.admin.staff.repository.StaffClientRepository;
import com.workbench.kato_system.admin.staff.repository.StaffRepository;
import com.workbench.kato_system.admin.utils.PageNumberUtils;
import com.workbench.kato_system.admin.utils.SearchUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffService {

  private final int SEARCH_SIZE = 30;

	private final StaffRepository staffRepository;
	private final StaffClientRepository staffClientRepository;

	public List<Staff> getAll() {
		return staffRepository.findAll();
	}

	public List<EmployeeDto> getEmployeeDtoByName(String name) {
		List<Staff> staffList = staffRepository.findByNameStartingWithOrNameKanaStartingWithAndDelFlgFalse(name, SearchUtils.HiraganaToKatakana(name));
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

  public Page<Staff> getSearchResult(StaffSearchForm form) {
		return staffRepository.findAll(Specification
				.where(delFlgIsFalse())
				.and(nameContains(form.getTargetName()))
        .and(departmentContains(form.getTargetDepartment()))
        .and(positionContains(form.getTargetPosition()))
        .and(joinYearPredicate(form.getTargetJoinYear(), form.getCondition()))
        .and(joinMonthPredicate(form.getTargetJoinMonth(), form.getCondition())),
				PageRequest.of(PageNumberUtils.revisePageNumber(form.getPageNumber()),
          SEARCH_SIZE, Sort.by(Sort.Direction.ASC, "id")));
	}

  public Specification<Staff> delFlgIsFalse() {
		return (root, query, cb) -> {
			query.distinct(true);
			return cb.isFalse(root.get("delFlg"));
		};
	}

  public Specification<Staff> nameContains(String name) {
		return !StringUtils.hasText(name) ? null : (root, query, cb) -> {
			return cb.or(cb.like(root.get("name"), "%" + name + "%"), 
        cb.like(root.get("nameKana"), "%" + SearchUtils.HiraganaToKatakana(name) + "%"));
		};
	}

  public Specification<Staff> departmentContains(String department) {
		return !StringUtils.hasText(department) ? null : (root, query, cb) -> {
			return cb.like(root.get("department"), "%" + department + "%");
		};
	}

  public Specification<Staff> positionContains(String position) {
		return !StringUtils.hasText(position) ? null : (root, query, cb) -> {
			return cb.like(root.get("position"), "%" + position + "%");
		};
	}

  public Specification<Staff> joinYearPredicate(Integer year, Integer condition) {
		return Objects.isNull(year) ? null : (root, query, cb) -> {
      String joinYear = "joinYear";
      switch (condition) {
        case 1:
          return cb.lessThanOrEqualTo(root.get(joinYear), year);
        case 2:
          return cb.equal(root.get(joinYear), year);
        case 3:
          return cb.greaterThanOrEqualTo(root.get(joinYear), year);
        default:
          break;
      }
      return null;
		};
	}

  public Specification<Staff> joinMonthPredicate(Integer month, Integer condition) {
		return Objects.isNull(month) ? null : (root, query, cb) -> {
			String joinMonth = "joinMonth";
      switch (condition) {
        case 1:
          return cb.lessThanOrEqualTo(root.get(joinMonth), month);
        case 2:
          return cb.equal(root.get(joinMonth), month);
        case 3:
          return cb.greaterThanOrEqualTo(root.get(joinMonth), month);
        default:
          break;
      }
      return null;
		};
	}

	public Staff save(StaffForm form) {

		Staff staff = new Staff();

		staff.setId(form.getId() == null ? null : form.getId());

		staff.setName(form.getLastName() + form.getFirstName());
    staff.setLastName(form.getLastName());
    staff.setFirstName(form.getFirstName());
    staff.setNameKana(form.getLastNameKana() + form.getFirstNameKana());
    staff.setLastNameKana(form.getLastNameKana());
    staff.setFirstNameKana(form.getFirstNameKana());
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

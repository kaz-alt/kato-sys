package com.workbench.kato_system.admin.employee.service;

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

import com.workbench.kato_system.admin.employee.dto.EmployeeDto;
import com.workbench.kato_system.admin.employee.form.EmployeeForm;
import com.workbench.kato_system.admin.employee.form.EmployeeSearchForm;
import com.workbench.kato_system.admin.employee.model.Employee;
import com.workbench.kato_system.admin.employee.model.EmployeeClient;
import com.workbench.kato_system.admin.employee.repository.EmployeeClientRepository;
import com.workbench.kato_system.admin.employee.repository.EmployeeRepository;
import com.workbench.kato_system.admin.utils.PageNumberUtils;
import com.workbench.kato_system.admin.utils.SearchUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

  private final int SEARCH_SIZE = 30;

	private final EmployeeRepository employeeRepository;
	private final EmployeeClientRepository employeeClientRepository;

	public List<Employee> getAll() {
		return employeeRepository.findAll();
	}

	public List<EmployeeDto> getEmployeeDtoByName(String name) {
		List<Employee> employeeList = employeeRepository.findByNameStartingWithOrNameKanaStartingWithAndDelFlgFalse(name, SearchUtils.HiraganaToKatakana(name));
		return model2Dto(employeeList);
	}

	public List<EmployeeDto> getEmployeeDtoByClientId(Integer clientId) {
		List<EmployeeClient> employeeClientList = employeeClientRepository.findByClientId(clientId);
		return employeeClient2Dto(employeeClientList);
	}

	public List<EmployeeDto> getSelectedEmployee(List<Integer> idList) {
		List<Employee> employeeList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(idList)) {
			employeeList = employeeRepository.findByIdIn(idList);
		}
		return model2Dto(employeeList);
	}

	public Page<Employee> getPageList(Pageable pageable) {
		if (pageable == null) {
			return null;
		}
		return employeeRepository.findAll(pageable);
	}

	public Employee getOne(Integer id) {
		return employeeRepository.findById(id).orElse(new Employee());
	}

  public Page<Employee> getSearchResult(EmployeeSearchForm form) {
		return employeeRepository.findAll(Specification
				.where(delFlgIsFalse())
				.and(nameContains(form.getTargetName()))
        .and(departmentContains(form.getTargetDepartment()))
        .and(positionContains(form.getTargetPosition()))
        .and(joinYearPredicate(form.getTargetJoinYear(), form.getCondition()))
        .and(joinMonthPredicate(form.getTargetJoinMonth(), form.getCondition())),
				PageRequest.of(PageNumberUtils.revisePageNumber(form.getPageNumber()),
          SEARCH_SIZE, Sort.by(Sort.Direction.ASC, "id")));
	}

  public Specification<Employee> delFlgIsFalse() {
		return (root, query, cb) -> {
			query.distinct(true);
			return cb.isFalse(root.get("delFlg"));
		};
	}

  public Specification<Employee> nameContains(String name) {
		return !StringUtils.hasText(name) ? null : (root, query, cb) -> {
			return cb.or(cb.like(root.get("name"), "%" + name + "%"), 
        cb.like(root.get("nameKana"), "%" + SearchUtils.HiraganaToKatakana(name) + "%"));
		};
	}

  public Specification<Employee> departmentContains(String department) {
		return !StringUtils.hasText(department) ? null : (root, query, cb) -> {
			return cb.like(root.get("department"), "%" + department + "%");
		};
	}

  public Specification<Employee> positionContains(String position) {
		return !StringUtils.hasText(position) ? null : (root, query, cb) -> {
			return cb.like(root.get("position"), "%" + position + "%");
		};
	}

  public Specification<Employee> joinYearPredicate(Integer year, Integer condition) {
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

  public Specification<Employee> joinMonthPredicate(Integer month, Integer condition) {
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

	public Employee save(EmployeeForm form) {

		Employee employee = new Employee();

		employee.setId(form.getId() == null ? null : form.getId());

		employee.setName(form.getLastName() + form.getFirstName());
    employee.setLastName(form.getLastName());
    employee.setFirstName(form.getFirstName());
    employee.setNameKana(form.getLastNameKana() + form.getFirstNameKana());
    employee.setLastNameKana(form.getLastNameKana());
    employee.setFirstNameKana(form.getFirstNameKana());
		employee.setDepartment(form.getDepartment());
		employee.setPosition(form.getPosition());
		employee.setTel(form.getTel());
		employee.setEmail(form.getEmail());
		employee.setJoinYear(form.getJoinYear());
		employee.setJoinMonth(form.getJoinMonth());
		employee.setDelFlg(false);

		employee = employeeRepository.save(employee);

		return employee;
	}

	public void delete(Integer id) {

		Optional<Employee> employee = employeeRepository.findById(id);
		if (employee.isPresent()) {
			Employee s = employee.get();
			s.setDelFlg(true);
			employeeRepository.save(s);
		}
	}

	private List<EmployeeDto> model2Dto(List<Employee> employeeList) {

		List<EmployeeDto> dtoList = new ArrayList<>();

		if (CollectionUtils.isEmpty(employeeList))
			return dtoList;

		for (Employee employee : employeeList) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(employee.getId());
			dto.setName(employee.getName());
			dtoList.add(dto);
		}

		return dtoList;
	}

	private List<EmployeeDto> employeeClient2Dto(List<EmployeeClient> employeeClientList) {

		List<EmployeeDto> dtoList = new ArrayList<>();

		if (CollectionUtils.isEmpty(employeeClientList))
			return dtoList;

		for (EmployeeClient sc : employeeClientList) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(sc.getEmployee().getId());
			dto.setName(sc.getEmployee().getName());
			dtoList.add(dto);
		}

		return dtoList;
	}

}

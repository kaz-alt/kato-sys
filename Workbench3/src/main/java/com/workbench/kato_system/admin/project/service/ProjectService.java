package com.workbench.kato_system.admin.project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.criteria.JoinType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.workbench.kato_system.admin.client.model.entity.Client;
import com.workbench.kato_system.admin.client.repository.ClientRepository;
import com.workbench.kato_system.admin.project.dto.ProjectDto;
import com.workbench.kato_system.admin.project.form.ProjectChangeProgressForm;
import com.workbench.kato_system.admin.project.form.ProjectForm;
import com.workbench.kato_system.admin.project.form.ProjectSearchForm;
import com.workbench.kato_system.admin.project.model.ApproachRoot;
import com.workbench.kato_system.admin.project.model.Factor;
import com.workbench.kato_system.admin.project.model.Progress;
import com.workbench.kato_system.admin.project.model.Project;
import com.workbench.kato_system.admin.project.model.ProjectEmployee;
import com.workbench.kato_system.admin.project.repository.ApproachRootRepository;
import com.workbench.kato_system.admin.project.repository.FactorRepository;
import com.workbench.kato_system.admin.project.repository.ProgressRepository;
import com.workbench.kato_system.admin.project.repository.ProjectEmployeeRepository;
import com.workbench.kato_system.admin.project.repository.ProjectRepository;
import com.workbench.kato_system.admin.security.LoginUserDetails;
import com.workbench.kato_system.admin.staff.model.Staff;
import com.workbench.kato_system.admin.staff.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

	private final ApproachRootRepository approachRootRepository;
	private final ClientRepository clientRepository;
	private final FactorRepository factorRepository;
	private final ProgressRepository progressRepository;
	private final ProjectRepository projectRepository;
	private final StaffRepository staffRepository;
	private final ProjectEmployeeRepository projectEmployeeRepository;

	private final int SEARCH_SIZE = 10;

	public Page<Project> getPageList(Pageable pageable) {
		if (pageable == null) {
			return null;
		}
		return projectRepository.findAll(pageable);
	}

	public Project getOne(Integer id) {
		Optional<Project> project = projectRepository.findByProjectId(id);
		return project.orElse(null);
	}

	public Set<Project> getListByClientIdAndExpectedOrderDate(Integer clientId, LocalDate startDate,
			LocalDate endDate) {

		return projectRepository.findByClientIdAndExpectedOrderDate(clientId, startDate, endDate);
	}

  public List<ProjectDto> getProjectDtoByName(String name) {

		return projectRepository.fetchDtoByName(name);
	}

  public List<Project> getSelectedProject(List<Integer> idList) {

		return projectRepository.findByIdIn(idList);
	}

	public List<ApproachRoot> getAllApproachRoot() {
		return approachRootRepository.findAll();
	}

	public List<Factor> getAllFactor() {
		return factorRepository.findAll();
	}

	public List<Progress> getAllProgress() {
		return progressRepository.findAll();
	}

	public Project save(ProjectForm form, LoginUserDetails user) {

		Project project = form2Project(form, user);

		return project;
	}

	public void delete(Integer id) {

		Optional<Project> project = projectRepository.findById(id);

		if (project.isPresent()) {
			Project p = project.get();
			p.setDelFlg(true);
			projectRepository.save(p);
		}
	}

	public void changeProgress(ProjectChangeProgressForm form) {

		Optional<Project> project = projectRepository.findById(form.getProjectId());

		if (project.isPresent()) {
			Project p = project.get();
			p.setProgressId(form.getProgressId());
			projectRepository.save(p);
		}

	}

	public Page<Project> getSearchResult(Integer pageNum, ProjectSearchForm form) {
		return projectRepository.findAll(Specification
				.where(delFlgIsFalse())
				.and(clientIdContains(form.getClientIdList()))
				.and(nameContains(form.getTargetProjectName()))
				.and(employeeIdContain(form.getOurEmployeeIdList()))
				.and(progressContains(form.getProgressIdList()))
				.and(orderProbabilityGreaterThan(form.getStartOrderProbability()))
				.and(orderProbabilityLessThan(form.getEndOrderProbability()))
				.and(estimatedOrderAmountGreaterThan(form.getStartEstimatedOrderAmount()))
				.and(estimatedOrderAmountLessThan(form.getEndEstimatedOrderAmount()))
				.and(expectedOrderDateGreaterThan(form.getStartExpectedOrderDate()))
				.and(expectedOrderDateLessThan(form.getEndExpectedOrderDate()))
				.and(approachRootContains(form.getApproachRootIdList())),
				PageRequest.of(pageNum, SEARCH_SIZE, Sort.by(
						form.getSortOrder().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
						form.getSortData())));
	}

	public Specification<Project> delFlgIsFalse() {
		return (root, query, cb) -> {
			if (Long.class != query.getResultType()) {
				query.distinct(true);
				root.fetch("client", JoinType.INNER);
				root.fetch("progress", JoinType.INNER);
				root.fetch("approachRoot", JoinType.INNER);
				root.fetch("factor", JoinType.LEFT);
				root.fetch("projectEmployee", JoinType.LEFT).fetch("staff", JoinType.LEFT);
			}
			return cb.isFalse(root.get("delFlg"));
		};
	}

	public Specification<Project> clientIdContains(List<Integer> clientIdList) {
		return CollectionUtils.isEmpty(clientIdList) ? null : (root, query, cb) -> {
			return root.get("clientId").in(clientIdList);
		};
	}

	public Specification<Project> nameContains(String name) {
		return !StringUtils.hasText(name) ? null : (root, query, cb) -> {
			return cb.like(root.get("name"), "%" + name + "%");
		};
	}

	public Specification<Project> employeeIdContain(List<Integer> employeeIdList) {
		return CollectionUtils.isEmpty(employeeIdList) ? null : (root, query, cb) -> {
			return root.join("projectEmployee", JoinType.INNER).get("staffId").in(employeeIdList);
		};
	}

	public Specification<Project> progressContains(List<Integer> progressList) {
		return CollectionUtils.isEmpty(progressList) ? null : (root, query, cb) -> {
			return root.get("progressId").in(progressList);
		};
	}

	public Specification<Project> orderProbabilityGreaterThan(Integer orderProbability) {
		return orderProbability == null ? null : (root, query, cb) -> {
			return cb.greaterThanOrEqualTo(root.get("orderProbability"), orderProbability);
		};
	}

	public Specification<Project> orderProbabilityLessThan(Integer orderProbability) {
		return orderProbability == null ? null : (root, query, cb) -> {
			return cb.lessThanOrEqualTo(root.get("orderProbability"), orderProbability);
		};
	}

	public Specification<Project> estimatedOrderAmountGreaterThan(Integer estimatedOrderAmount) {
		return estimatedOrderAmount == null ? null : (root, query, cb) -> {
			return cb.greaterThanOrEqualTo(root.get("estimatedOrderAmount"), estimatedOrderAmount);
		};
	}

	public Specification<Project> estimatedOrderAmountLessThan(Integer estimatedOrderAmount) {
		return estimatedOrderAmount == null ? null : (root, query, cb) -> {
			return cb.lessThanOrEqualTo(root.get("estimatedOrderAmount"), estimatedOrderAmount);
		};
	}

	public Specification<Project> expectedOrderDateGreaterThan(LocalDate startDate) {
		return startDate == null ? null : (root, query, cb) -> {
			return cb.greaterThanOrEqualTo(root.get("expectedOrderDate"), startDate);
		};
	}

	public Specification<Project> expectedOrderDateLessThan(LocalDate endDate) {
		return endDate == null ? null : (root, query, cb) -> {
			return cb.lessThanOrEqualTo(root.get("expectedOrderDate"), endDate);
		};
	}

	public Specification<Project> approachRootContains(List<Integer> approachRootIdList) {
		return CollectionUtils.isEmpty(approachRootIdList) ? null : (root, query, cb) -> {
			return root.get("approachRootId").in(approachRootIdList);
		};
	}

	private Project form2Project(ProjectForm form, LoginUserDetails user) {

		Project project = new Project();

		if (form.getId() != null) {
			project = projectRepository.findById(form.getId()).orElse(new Project());
			projectEmployeeRepository.deleteByProjectId(project.getId());
		}

		project.setClientId(form.getClientId());
		Client client = clientRepository.findByClientId(form.getClientId());
		project.setClient(client);

		project.setApproachRootId(form.getApproachRoot());
		ApproachRoot approachRoot = approachRootRepository.findById(form.getApproachRoot()).orElse(new ApproachRoot());
		project.setApproachRoot(approachRoot);

		if (form.getProgress() != null) {
			project.setProgressId(form.getProgress());
			Progress progress = progressRepository.findById(form.getProgress()).orElse(new Progress());
			project.setProgress(progress);
		}

		if (form.getFactor() != null) {
			project.setFactorId(form.getFactor());
			Factor factor = factorRepository.findById(form.getFactor()).orElse(new Factor());
			project.setFactor(factor);
		}

		project.setName(form.getProjectName());
		project.setOrderProbability(form.getOrderProbability());
		project.setEstimatedOrderAmount(form.getEstimatedOrderAmount());
		project.setExpectedOrderDate(form.getExpectedOrderDate());
		project.setCompetitor(form.getCompetitor());
		project.setDelFlg(false);
		project = projectRepository.save(project);

		Set<ProjectEmployee> projectEmployee = form2ProjectEmployee(form, project, user);
		project.setProjectEmployee(projectEmployee);

		projectRepository.save(project);

		return project;
	}

	private Set<ProjectEmployee> form2ProjectEmployee(ProjectForm form, Project project, LoginUserDetails user) {

		Set<ProjectEmployee> set = new HashSet<>();

		List<Staff> staffList = staffRepository.findByIdIn(form.getClientStaffIdList());

		for (Staff staff : staffList) {
			ProjectEmployee pe = new ProjectEmployee();
			pe.setStaffId(staff.getId());
			pe.setStaff(staff);
			pe.setProjectId(project.getId());
			pe.setProject(project);
			pe.setCreatedDate(LocalDateTime.now());
			pe.setCreatedBy(user.getUsername());
			set.add(pe);
		}

		return set;
	}

}

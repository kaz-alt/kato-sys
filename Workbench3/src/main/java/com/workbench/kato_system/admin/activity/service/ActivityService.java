package com.workbench.kato_system.admin.activity.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.JoinType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.workbench.kato_system.admin.activity.form.ActivityForm;
import com.workbench.kato_system.admin.activity.form.ActivitySearchForm;
import com.workbench.kato_system.admin.activity.model.Activity;
import com.workbench.kato_system.admin.activity.repository.ActivityRepository;
import com.workbench.kato_system.admin.employee.model.Employee;
import com.workbench.kato_system.admin.employee.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

	private final ActivityRepository activityRepository;

	private final int SEARCH_SIZE = 10;

	public List<Activity> getAll() {
		return activityRepository.findAll();
	}

	public Page<Activity> getPageList(Pageable pageable) {
		if (pageable == null) {
			return null;
		}
		return activityRepository.findPageList(pageable);
	}

	public Activity getOne(Integer id) {
		Optional<Activity> a = activityRepository.findEntityById(id);
		return a.orElse(new Activity());
	}

	public Activity save(ActivityForm form) {

		Activity activity = form2Activity(form);

		return activity;
	}

	public void delete(Integer id) {

		Optional<Activity> a = activityRepository.findById(id);
		if (a.isPresent()) {
			Activity activity = a.get();
			activityRepository.delete(activity);
		}
	}

	public Page<Activity> getSearchResult(Integer pageNum, ActivitySearchForm form) {
		return activityRepository.findAll(Specification
        .where(fetchRelationEntity())
				.and(employeeIdContains(form.getEmployeeIdList()))
				.and(dateGreaterThan(form.getStartActivityDate()))
				.and(dateLessThan(form.getEndActivityDate()))
        .and(contentContains(form.getTargetContent()))
        .and(projectIdContains(form.getProjectIdList())),
        PageRequest.of(pageNum, SEARCH_SIZE, Sort.by(
          form.getSortOrder().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
          form.getSortData())));
	}

  public Specification<Activity> fetchRelationEntity() {
		return (root, query, cb) -> {
      if (Long.class != query.getResultType()) {
				query.distinct(true);
				root.fetch("employee", JoinType.INNER);
        root.fetch("project", JoinType.LEFT);
			}
			return cb.conjunction();
		};
	}

	public Specification<Activity> employeeIdContains(List<Integer> employeeIdList) {
		return CollectionUtils.isEmpty(employeeIdList) ? null : (root, query, cb) -> {
			return root.get("employeeId").in(employeeIdList);
		};
	}

	public Specification<Activity> dateGreaterThan(LocalDate startDate) {
		return startDate == null ? null : (root, query, cb) -> {
			return cb.greaterThanOrEqualTo(root.get("activityDate").as(LocalDate.class), startDate);
		};
	}

	public Specification<Activity> dateLessThan(LocalDate endDate) {
		return endDate == null ? null : (root, query, cb) -> {
			return cb.lessThanOrEqualTo(root.get("activityDate").as(LocalDate.class), endDate);
		};
	}

  public Specification<Activity> contentContains(String content) {
		return !StringUtils.hasText(content)? null : (root, query, cb) -> {
			return cb.like(root.get("content"), "%" + content + "%");
		};
  }

  public Specification<Activity> projectIdContains(List<Integer> projectIdList) {
		return CollectionUtils.isEmpty(projectIdList) ? null : (root, query, cb) -> {
			return root.get("projectId").in(projectIdList);
		};
	}

	private Activity form2Activity(ActivityForm form) {

		Activity a = new Activity();

		if (form.getId() != null) {
			a = activityRepository.findById(form.getId()).orElse(new Activity());
		}

		a.setEmployeeId(form.getEmployeeId());
		a.setActivityDate(form.getActivityDate());
		a.setContent(form.getContent());
		a.setProjectId(form.getProjectId());
		a = activityRepository.save(a);

		return a;
	}

}

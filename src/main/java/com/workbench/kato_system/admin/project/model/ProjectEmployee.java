package com.workbench.kato_system.admin.project.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.workbench.kato_system.admin.employee.model.Employee;

import lombok.Getter;
import lombok.Setter;

/**
 * 社員が担当する顧客
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "project_employee")
@Audited
@Setter
@Getter
public class ProjectEmployee implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", insertable = false, updatable = false)
	private Project project;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", insertable = false, updatable = false)
	private Employee employee;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 社員ID */
	@Column(name = "employee_id")
	private Integer employeeId;

	/* 案件ID */
	@Column(name = "project_id")
	private Integer projectId;

	/* 作成者 */
	@Column(name = "created_by")
	private String createdBy;

	/* 作成日時 */
	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;

}

package com.workbench.kato_system.admin.timeline.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import com.workbench.kato_system.admin.employee.model.Employee;

import lombok.Data;

/**
 * タイムライン
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "timeline")
@Data
public class Timeline implements Serializable {

	@OneToMany(mappedBy = "timeline", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<TimelineResponse> timelineResponse;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", insertable = false, updatable = false)
	private Employee employee;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 社員ID */
	@Column(name = "employee_id")
	private Integer employeeId;

	/* 内容 */
	@Column(name = "content")
	private String content;

	/* 作成日時 */
	@Column(name = "created_date", nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime createdDate;

}

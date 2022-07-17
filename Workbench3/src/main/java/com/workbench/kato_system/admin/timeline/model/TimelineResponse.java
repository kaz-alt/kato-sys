package com.workbench.kato_system.admin.timeline.model;

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

import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

/**
 * スケジュール
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "timeline_response")
@Data
public class TimelineResponse implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", insertable = false, updatable = false)
	private Timeline timeline;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "timeline_id")
	private Integer timelineId;

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

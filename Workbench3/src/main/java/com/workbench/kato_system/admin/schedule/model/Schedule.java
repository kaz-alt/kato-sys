package com.workbench.kato_system.admin.schedule.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

/**
 * スケジュール
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "schedule")
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
public class Schedule implements Serializable {

	/* 担当者 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy ="schedule", cascade = CascadeType.ALL)
	private List<ScheduleEmployee> scheduleEmployee;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 開始時間 */
	@Column(name = "start_time")
	private LocalDateTime startTime;

	/* 終了時間 */
	@Column(name = "end_time")
	private LocalDateTime endTime;

	/* タイトル */
	@Column(name = "title")
	private String title;

	/* 詳細 */
	@Column(name = "detail")
	private String detail;

	/* 場所 */
	@Column(name = "place")
	private String place;

	/* 終日フラグ */
	@Column(name = "is_all_day")
	private Boolean isAllDay;

	/* 作成者 */
	@Column(name = "created_by")
	@CreatedBy
	private String createdBy;

	/* 作成日時 */
	@Column(name = "created_date", nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime createdDate;

	/* 更新者 */
	@Column(name = "modified_by")
	@LastModifiedBy
	private String modifiedBy;

	/* 更新日時 */
	@Column(name = "modified_date")
	@LastModifiedDate
	private LocalDateTime modifiedDate;

}

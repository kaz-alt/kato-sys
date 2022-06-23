package com.workbench.kato_system.admin.schedule.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.workbench.kato_system.admin.staff.model.Staff;

import lombok.Data;

/**
 * スケジュールに登録する社員
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "schedule_employee")
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
public class ScheduleEmployee implements Serializable {

	@ManyToOne
	@JoinColumn(name = "schedule_id", insertable = false, updatable = false)
	private Schedule schedule;

	/* 担当者 */
	@ManyToOne
	@JoinColumn(name = "staff_id", insertable = false, updatable = false)
	private Staff staff;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 開始時間 */
	@Column(name = "schedule_id")
	private Integer scheduleId;

	/* 終了時間 */
	@Column(name = "staff_id")
	private Integer staffId;

}

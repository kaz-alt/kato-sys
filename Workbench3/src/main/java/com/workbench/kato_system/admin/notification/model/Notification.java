package com.workbench.kato_system.admin.notification.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 通知
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "notification")
@Data
public class Notification implements Serializable {

	/* 社員ID */
	@Id
	@Column(name = "employee_id")
	private Integer employeeId;

	/* タイムラインID */
	@Column(name = "timeline_id")
	private Integer timelineId;

	/* 確認日時 */
	@Column(name = "check_time")
	private LocalDateTime checkTime;
}

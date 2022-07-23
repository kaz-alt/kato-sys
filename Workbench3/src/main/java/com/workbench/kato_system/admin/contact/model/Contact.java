package com.workbench.kato_system.admin.contact.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

/**
 * 報告
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "contact")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Contact implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 社員ID */
	@Column(name = "employee_id")
	private Integer employeeId;

	/* タイプ */
	@Column(name = "type")
	private String type;

	/* ToDo */
	@Column(name = "content")
	private String content;

	/* 作成日 */
	@Column(name = "created_date")
	@CreatedDate
	private LocalDateTime createdDate;

}

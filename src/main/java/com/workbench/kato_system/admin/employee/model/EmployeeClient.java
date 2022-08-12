package com.workbench.kato_system.admin.employee.model;

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

import com.workbench.kato_system.admin.client.model.entity.Client;

import lombok.Data;

/**
 * 社員が担当する顧客
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "employee_client")
@Audited
@Data
public class EmployeeClient implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", insertable = false, updatable = false)
	private Employee employee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", insertable = false, updatable = false)
	private Client client;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 社員ID */
	@Column(name = "employee_id")
	private Integer employeeId;

	/* 顧客ID */
	@Column(name = "client_id")
	private Integer clientId;

	/* 作成者 */
	@Column(name = "created_by")
	private String createdBy;

	/* 作成日時 */
	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;

}

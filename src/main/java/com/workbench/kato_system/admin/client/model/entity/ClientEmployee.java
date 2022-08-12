package com.workbench.kato_system.admin.client.model.entity;

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

import com.workbench.kato_system.admin.client.model.enums.Motivation;
import com.workbench.kato_system.admin.client.model.enums.Standpoint;

import lombok.Data;

/**
 * 顧客担当者
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "client_employee")
@Audited
@Data
public class ClientEmployee implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", insertable = false, updatable = false)
	private Client client;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 顧客ID */
	@Column(name = "client_id")
	private Integer clientId;

	/* 氏名 */
	@Column(name = "name")
	private String name;

	/* 担当部署 */
	@Column(name = "department")
	private String department;

	/* 役職 */
	@Column(name = "position")
	private String position;

	/* 電話番号 */
	@Column(name = "tel")
	private String tel;

	/* メールアドレス */
	@Column(name = "email")
	private String email;

	/* 立場 */
	@Column(name = "standpoint")
	private Integer standpoint;

	/* 意欲 */
	@Column(name = "motivation")
	private Integer motivation;

	/* 備考 */
	@Column(name = "remarks")
	private String remarks;

	/* 作成者 */
	@Column(name = "created_by")
	private String createdBy;

	/* 作成日時 */
	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;

	public String getStandpointValue() {
		return Standpoint.valueOf(this.standpoint).getName();
	}

	public String getMotivationValue() {
		return Motivation.valueOf(this.motivation).getName();
	}
}

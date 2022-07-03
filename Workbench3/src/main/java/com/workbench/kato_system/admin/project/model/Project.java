package com.workbench.kato_system.admin.project.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.workbench.kato_system.admin.activity.model.Activity;
import com.workbench.kato_system.admin.client.model.entity.Client;

import lombok.Getter;
import lombok.Setter;

/**
 * 案件
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "project")
@Audited
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
public class Project implements Serializable {

	/* 顧客 */
	@NotAudited
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", insertable = false, updatable = false)
	private Client client;

	/* 進捗 */
	@NotAudited
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "progress_id", insertable = false, updatable = false)
	private Progress progress;

	/* アプローチルート */
	@NotAudited
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approach_root_id", insertable = false, updatable = false)
	private ApproachRoot approachRoot;

	/* 勝敗要因 */
	@NotAudited
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "factor_id", insertable = false, updatable = false)
	private Factor factor;

	/* 案件担当者 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
	private Set<ProjectEmployee> projectEmployee;

	/* 活動記録 */
	@NotAudited
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
	private Set<Activity> activity;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 顧客ID */
	@Column(name = "client_id")
	private Integer clientId;

	/* 案件名 */
	@Column(name = "name")
	private String name;

	/* 進捗ID */
	@Column(name = "progress_id")
	private Integer progressId;

	/* 受注確度 */
	@Column(name = "order_probability")
	private Integer orderProbability;

	/* 受注（予定）金額 */
	@Column(name = "estimated_order_amount")
	private Integer estimatedOrderAmount;

	/* 受注（予定）日 */
	@Column(name = "expected_order_date")
	private LocalDate expectedOrderDate;

	/* アプローチルートID */
	@Column(name = "approach_root_id")
	private Integer approachRootId;

	/* 競合先 */
	@Column(name = "competitor")
	private String competitor;

	/* 勝敗要因 */
	@Column(name = "factor_id")
	private Integer factorId;

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

	/* 削除フラグ */
	@Column(name = "del_flg")
	private Boolean delFlg;

	public Progress getSuperProgress() {
		return this.progress;
	}
}

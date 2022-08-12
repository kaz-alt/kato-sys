package com.workbench.kato_system.admin.inquiry_requirement_complaint.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.workbench.kato_system.admin.client.model.entity.Client;
import com.workbench.kato_system.admin.employee.model.Employee;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.model.enums.ContentType;

import lombok.Data;

/**
 * 問い合わせ・要求・クレーム
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "inquiry_requirement_complaint")
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
public class InquiryRequirementComplaint {

	/* 顧客 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", insertable = false, updatable = false)
	private Client client;

	/* 社員 */
	@OneToOne
	@JoinColumn(name = "responsible_employee_id", insertable = false, updatable = false)
	private Employee employee;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 顧客ID */
	@Column(name = "client_id")
	private Integer clientId;

	/* タイプ */
	@Column(name = "content_type")
	private Integer contentType;

	/* 内容 */
	@Column(name = "content")
	private String content;

	/* 発生日 */
	@Column(name = "occurred_date")
	private LocalDate occurredDate;

	/* 担当責任者ID */
	@Column(name = "responsible_employee_id")
	private Integer responsibleEmployeeId;

	/* 解決済かどうか */
	@Column(name = "has_solved")
	private Boolean hasSolved;

	/* 解決日 */
	@Column(name = "solved_date")
	private LocalDate solvedDate;

	/* 解決内容 */
	@Column(name = "solved_content")
	private String solvedContent;

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

	/* タイプ名を返す */
	public String getContentTypeValue() {
		return ContentType.valueOf(this.contentType).getName();
	}

}

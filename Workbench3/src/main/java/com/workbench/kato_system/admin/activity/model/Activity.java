package com.workbench.kato_system.admin.activity.model;

import java.io.Serializable;
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

import org.hibernate.annotations.Immutable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.workbench.kato_system.admin.project.model.Project;
import com.workbench.kato_system.admin.staff.model.Staff;

import lombok.Getter;
import lombok.Setter;

/**
 * 活動記録
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "activity")
@Audited
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Activity implements Serializable {

	/* 社員 */
	@NotAudited
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "staff_id", insertable = false, updatable = false)
	private Staff staff;

	/* 関連案件 */
	@NotAudited
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Project project;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 社員ID */
	@Column(name = "staff_id")
	private Integer staffId;

	/* 活動日 */
	@Column(name = "activity_date")
	private LocalDate activityDate;

	/* 活動内容 */
	@Column(name = "content")
	private String content;

	/* 案件ID */
	@Column(name = "project_id")
	private Integer projectId;

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

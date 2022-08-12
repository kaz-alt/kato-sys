package com.workbench.kato_system.admin.client.model.entity;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.workbench.kato_system.admin.client.model.enums.ClientType;
import com.workbench.kato_system.admin.client.model.enums.Industry;
import com.workbench.kato_system.admin.employee.model.EmployeeClient;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.model.entity.InquiryRequirementComplaint;
import com.workbench.kato_system.admin.product.model.Product;
import com.workbench.kato_system.admin.project.model.Project;

import lombok.Getter;
import lombok.Setter;

/**
 * 顧客
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "client")
@Audited
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
public class Client implements Serializable {

	/* 案件 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
	private Set<Project> projectList;

	/* 顧客担当者 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
	private Set<ClientEmployee> clientEmployeeList;

	/* 当社担当者 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
	private Set<EmployeeClient> employeeClientList;

	/* 製品・サービス */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.REMOVE)
	private Set<Product> productList;

	/* 問い合わせ・要求・クレーム */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.REMOVE)
	private Set<InquiryRequirementComplaint> inquiryRequirementComplaintList;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id = 0;

	/* 顧客名 */
	@Column(name = "name")
	private String name;

	/* 業種 */
	@Column(name = "industry")
	private Integer industry;

	/* 顧客タイプ */
	@Column(name = "client_type")
	private Integer clientType;

	/* 所在地 */
	@Column(name = "address")
	private String address;

	/* URL */
	@Column(name = "url")
	private String url;

	/* 初回面談日 */
	@Column(name = "first_interview_date")
	private LocalDate firstInterviewDate;

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

	public String getValueOfIndustry() {
		return Industry.valueOf(this.industry).getName();
	}

	public String getValueOfClientType() {
		return ClientType.valueOf(this.clientType).getName();
	}

}

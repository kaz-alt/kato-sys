package com.workbench.kato_system.admin.business_card.model;

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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.workbench.kato_system.admin.client.model.entity.Client;
import com.workbench.kato_system.admin.client.model.entity.ClientStaff;

import lombok.Data;

/**
 * 名刺
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "business_card")
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
public class BusinessCard implements Serializable {

	/* 顧客 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", insertable = false, updatable = false)
	private Client client;

	/* 顧客担当者 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_staff_id", insertable = false, updatable = false)
	private ClientStaff clientStaff;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 顧客ID */
	@Column(name = "client_id")
	private Integer clientId;

	/* 顧客担当者ID */
	@Column(name = "client_staff_id")
	private Integer clientStaffId;

	/* 会社名 */
	@Column(name = "company_name")
	private String companyName;

	/* 氏名 */
	@Column(name = "name")
	private String name;

	/* 氏名カナ */
	@Column(name = "name_kana")
	private String nameKana;

	/* 部署 */
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

	/* 交換日 */
	@Column(name = "exchange_date")
	private LocalDate exchangeDate;

	/* ファイル名 */
	@Column(name = "file_name")
	private String fileName;

	/* コンテントタイプ */
	@Column(name = "content_type")
	private String contentType;

	/* 画像データ */
	@Lob
	@Type(type = "org.hibernate.type.BinaryType")
	@Column(name = "image", columnDefinition = "blob", length = 16777215)
	private byte[] image;

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
}

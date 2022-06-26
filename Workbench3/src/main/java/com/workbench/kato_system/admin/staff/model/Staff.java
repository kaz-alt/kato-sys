package com.workbench.kato_system.admin.staff.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

/**
 * 社員
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "staff")
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
public class Staff implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 氏名 */
	@Column(name = "name")
	private String name;

  /* 姓 */
	@Column(name = "last_name")
	private String lastName;

  /* 名 */
	@Column(name = "first_name")
	private String firstName;

	/* 氏名カナ */
	@Column(name = "name_kana")
	private String nameKana;

  /* 姓 */
	@Column(name = "last_name_kana")
	private String lastNameKana;

  /* 名 */
	@Column(name = "first_name_kana")
	private String firstNameKana;

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

	/* 入社年 */
	@Column(name = "join_year")
	private Integer joinYear;

	/* 入社月 */
	@Column(name = "join_month")
	private Integer joinMonth;

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

package com.workbench.kato_system.admin.product.model;

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
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.workbench.kato_system.admin.client.model.entity.Client;

import lombok.Data;

/**
 * 製品・サービス
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "product")
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
public class Product implements Serializable {

	/* 顧客 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", insertable = false, updatable = false)
	private Client client;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 製品・サービス名 */
	@Column(name = "name")
	private String name;

	/* 数量 */
	@Column(name = "quantity")
	private Integer quantity;

	/* 売上額 */
	@Column(name = "sales")
	private Integer sales;

	/* 受注日 */
	@Column(name = "purchased_date")
	private LocalDate purchasedDate;

	/* 備考 */
	@Column(name = "remarks")
	private String remarks;

	/* 顧客ID */
	@Column(name = "client_id")
	private Integer clientId;

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

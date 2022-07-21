package com.workbench.kato_system.admin.timeline.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.workbench.kato_system.admin.employee.model.Employee;

import lombok.Data;

/**
 * タイムライン
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "timeline")
@Data
public class Timeline implements Serializable {

	@OneToMany(mappedBy = "timeline", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<TimelineResponse> timelineResponse;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", insertable = false, updatable = false)
	private Employee employee;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 社員ID */
	@Column(name = "employee_id")
	private Integer employeeId;

	/* 内容 */
	@Column(name = "content")
	private String content;

	/* 作成日時 */
	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;

	/* 画像データ */
	@Lob
	@Type(type = "org.hibernate.type.BinaryType")
	@Column(name = "image", columnDefinition = "blob", length = 16777215)
	private byte[] image;

	public String getStringImage() {
		return Base64.getEncoder().encodeToString(this.image);
	}

}

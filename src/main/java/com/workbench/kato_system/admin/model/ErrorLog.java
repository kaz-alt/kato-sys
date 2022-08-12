package com.workbench.kato_system.admin.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * エラー
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "error_log")
@Getter
@Setter
@NoArgsConstructor
public class ErrorLog implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* エラーパス */
	@Column(name = "error_path")
	private String errorPath;

	/* 作成者 */
	@Column(name = "created_by")
	private String createdBy;

	/* 作成日時 */
	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;

  public ErrorLog(String errorPath, String createdBy, LocalDateTime createdDateTime) {
    this.errorPath = errorPath;
    this.createdBy = createdBy;
    this.createdDate = createdDateTime;
  }
}

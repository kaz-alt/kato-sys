package com.workbench.kato_system.admin.project.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 進捗
 * @author katoukazuya
 *
 */
@Entity
@Table(name = "progress")
@Data
public class Progress implements Serializable {

	@Id
	@Column(name = "id")
	private Integer id;

	/* 名称 */
	@Column(name = "name")
	private String name;
}

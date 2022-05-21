package com.workbench.kato_system.admin.business_card.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BusinessCardDto {

	/* 顧客担当者ID */
	private Integer clientStaffId;

	/* 会社名 */
	private String companyName;

	/* 氏名 */
	private String name;

	/* 氏名カナ */
	private String nameKana;

	/* 部署 */
	private String department;

	/* 役職 */
	private String position;

	/* 電話番号 */
	private String tel;

	/* メールアドレス */
	private String email;

	/* 交換日 */
	private LocalDate exchangeDate;

	/* 画像データ */
	private String image;
}

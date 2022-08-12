package com.workbench.kato_system.admin.client.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 顧客業種
 * @author katoukazuya
 *
 */
@AllArgsConstructor
@Getter
public enum Industry {
	MAKER(1, "メーカー"), TRAIDING_COMPANY(2, "商社"), RETAIL(3, "小売"), FINANCIAL(4, "金融"), SERVICE(5, "サービス"), MEDIA(6,
			"マスコミ"), SOFTWARE_COMMUNICATION(7, "ソフトウェア・通信"), GOVERMENT_OFFICE(8, "官公庁・公社・団体");

	private int value;
	private String name;

	public static Industry valueOf(int value) {
		for (Industry i : Industry.values()) {
			if (i.value == value) {
				return i;
			}
		}
		return null;
	}
}

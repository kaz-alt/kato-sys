package com.workbench.kato_system.admin.client.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 顧客タイプ
 * @author katoukazuya
 *
 */
@AllArgsConstructor
@Getter
public enum ClientType {
	POTENTIAL(1, "見込み"), NEW(2, "新規"), EXISTING(3, "既存"), IMPORTANT(4, "重要"), SUSPENDED(5, "取引停止");

	private int value;
	private String name;

	public static ClientType valueOf(int value) {
		for (ClientType ct : ClientType.values()) {
			if (ct.value == value) {
				return ct;
			}
		}
		return null;
	}
}
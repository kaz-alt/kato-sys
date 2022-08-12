package com.workbench.kato_system.admin.client.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 意欲
 * @author katoukazuya
 *
 */
@AllArgsConstructor
@Getter
public enum Motivation {
	POSITIVE(1, "前向き"), NEUTRAL(2, "普通"), NEGATIVE(3, "後向き"), UNKNOWN(4, "不明");

	private int value;
	private String name;

	public static Motivation valueOf(int value) {
		for (Motivation m : Motivation.values()) {
			if (m.value == value) {
				return m;
			}
		}
		return null;
	}
}
package com.workbench.kato_system.admin.client.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 立場
 * @author katoukazuya
 *
 */
@AllArgsConstructor
@Getter
public enum Standpoint {
	AUTHORITHER(1, "決裁者"), PERSON(2, "担当者"), MEDITOR(3, "仲介者");

	private int value;
	private String name;

	public static Standpoint valueOf(int value) {
		for (Standpoint s : Standpoint.values()) {
			if (s.value == value) {
				return s;
			}
		}
		return null;
	}
}
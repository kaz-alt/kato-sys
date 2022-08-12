package com.workbench.kato_system.admin.inquiry_requirement_complaint.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 問い合わせ・要望・クレームタイプ
 * @author katoukazuya
 *
 */
@AllArgsConstructor
@Getter
public enum ContentType {
	INQUIRY(1, "問い合わせ"), REQUIREMNT(2, "要望"), COMPLAINT(3, "クレーム");

	private int value;
	private String name;

	public static ContentType valueOf(int value) {
		for (ContentType ct : ContentType.values()) {
			if (ct.value == value) {
				return ct;
			}
		}
		return null;
	}
}
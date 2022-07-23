package com.workbench.kato_system.admin.contact.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ContactForm {

	@NotEmpty(message ="必須入力です")
	private String type;

	@Size(max = 100, message = "100字以内で入力してください")
	private String content;

}

package com.workbench.kato_system.admin.contact.service;

import org.springframework.stereotype.Service;

import com.workbench.kato_system.admin.contact.form.ContactForm;
import com.workbench.kato_system.admin.contact.model.Contact;
import com.workbench.kato_system.admin.contact.repository.ContactRepository;
import com.workbench.kato_system.admin.login.model.LoginUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {

	private final ContactRepository contactRepository;

	public void save(ContactForm form, LoginUserDetails user) {

		Contact c = new Contact();
		c.setEmployeeId(user.getUserId());
		c.setType(form.getType());
		c.setContent(form.getContent());

		contactRepository.save(c);

	}

}

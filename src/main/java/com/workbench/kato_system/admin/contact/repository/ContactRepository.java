package com.workbench.kato_system.admin.contact.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workbench.kato_system.admin.contact.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

}

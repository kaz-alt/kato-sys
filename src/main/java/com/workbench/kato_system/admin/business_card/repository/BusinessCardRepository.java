package com.workbench.kato_system.admin.business_card.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.workbench.kato_system.admin.business_card.model.BusinessCard;

public interface BusinessCardRepository
		extends JpaRepository<BusinessCard, Integer>, JpaSpecificationExecutor<BusinessCard> {

	@Override
	@Query("select bc from BusinessCard bc where bc.delFlg = 0")
	Page<BusinessCard> findAll(Pageable pageable);

}

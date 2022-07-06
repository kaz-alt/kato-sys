package com.workbench.kato_system.admin.inquiry_requirement_complaint.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.inquiry_requirement_complaint.model.entity.InquiryRequirementComplaint;

public interface InquiryRequirementComplaintRepository
		extends JpaRepository<InquiryRequirementComplaint, Integer>,
		JpaSpecificationExecutor<InquiryRequirementComplaint> {

	@Override
	@Query("select distinct i "
			+ "from InquiryRequirementComplaint i "
			+ "inner join fetch i.client c "
			+ "inner join fetch i.employee s "
			+ "order by i.clientId")
	List<InquiryRequirementComplaint> findAll();

	@Override
	@Query(value = "select distinct i "
			+ "from InquiryRequirementComplaint i "
			+ "inner join fetch i.client c "
			+ "inner join fetch i.employee s "
			+ "where i.delFlg = 0 "
			+ "order by i.clientId, i.occurredDate", countQuery = "select count(distinct i) "
					+ "from InquiryRequirementComplaint i "
					+ "inner join i.client c "
					+ "inner join i.employee s "
					+ "where i.delFlg = 0 "
					+ "order by i.clientId, i.occurredDate")
	Page<InquiryRequirementComplaint> findAll(Pageable pageable);

	@Query("select distinct i from InquiryRequirementComplaint i "
			+ "inner join fetch i.client c "
			+ "inner join fetch i.employee s "
			+ "where i.id = :id "
			+ "and i.delFlg = 0")
	InquiryRequirementComplaint findByInquiryRequirementComplaintId(@Param("id") Integer id);

	@Query("select distinct i from InquiryRequirementComplaint i "
			+ "inner join fetch i.client c "
			+ "inner join fetch i.employee s "
			+ "where c.id = :clientId "
			+ "order by i.occurredDate")
	List<InquiryRequirementComplaint> findByClientId(@Param("clientId") Integer clientId);

	@Query("select distinct i from InquiryRequirementComplaint i "
			+ "inner join fetch i.client c "
			+ "inner join fetch i.employee s "
			+ "where i.delFlg = 0 "
			+ "and c.id = :clientId "
			+ "and i.occurredDate >= :startOccurredDate "
			+ "and i.occurredDate <= :endOccurredDate "
			+ "order by i.occurredDate")
	List<InquiryRequirementComplaint> findByClientIdAndOccurredDate(
			@Param("clientId") Integer clientId, @Param("startOccurredDate") LocalDate startOccurredDate,
			@Param("endOccurredDate") LocalDate endOccurredDate);

}

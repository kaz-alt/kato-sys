package com.workbench.kato_system.admin.inquiry_requirement_complaint.service;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.criteria.JoinType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.workbench.kato_system.admin.client.model.entity.Client;
import com.workbench.kato_system.admin.client.repository.ClientRepository;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.form.InquiryRequirementComplaintForm;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.form.InquiryRequirementComplaintSearchForm;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.model.entity.InquiryRequirementComplaint;
import com.workbench.kato_system.admin.inquiry_requirement_complaint.repository.InquiryRequirementComplaintRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryRequirementComplaintService {

	private final ClientRepository clientRepository;
	private final InquiryRequirementComplaintRepository inquiryRequirementComplaintRepository;

	private final int SEARCH_SIZE = 10;

	public List<InquiryRequirementComplaint> getAll() {
		return inquiryRequirementComplaintRepository.findAll();
	}

	public Page<InquiryRequirementComplaint> getPageList(Pageable pageable) {
		if (pageable == null) {
			return null;
		}
		return inquiryRequirementComplaintRepository.findAll(pageable);
	}

	public InquiryRequirementComplaint getOne(Integer id) {
		return inquiryRequirementComplaintRepository.findByInquiryRequirementComplaintId(id);
	}

	public List<InquiryRequirementComplaint> getByClientId(Integer clientId) {
		return inquiryRequirementComplaintRepository.findByClientId(clientId);
	}

	public List<InquiryRequirementComplaint> getByClientIdAndOccurredDate(Integer clientId,
			LocalDate startOccurredDate, LocalDate endOccurredDate) {
		return inquiryRequirementComplaintRepository.findByClientIdAndOccurredDate(clientId, startOccurredDate,
				endOccurredDate);
	}

	public InquiryRequirementComplaint save(InquiryRequirementComplaintForm form) {

		InquiryRequirementComplaint inquiry = form2Model(form);

		return inquiry;
	}

	public void delete(Integer id) {

		InquiryRequirementComplaint data = inquiryRequirementComplaintRepository
				.findByInquiryRequirementComplaintId(id);
		data.setDelFlg(true);
		inquiryRequirementComplaintRepository.save(data);
	}

	public Page<InquiryRequirementComplaint> getSearchResult(Integer pageNum,
			InquiryRequirementComplaintSearchForm form) {
		return inquiryRequirementComplaintRepository.findAll(Specification
				.where(delFlgIsFalse())
				.and(clientIdContains(form.getClientIdList()))
				.and(contentTypesEquals(form.getTargetContentType()))
				.and(occurredDateGreaterThan(form.getStartOccurredDate()))
				.and(occurredDateLessThan(form.getEndOccurredDate()))
				.and(hasSolved(form.getTargetHasSolved()))
				.and(responsibleEmployeeIdContains(form.getTargetResponsibleEmployeeIdList()))
				.and(solvedDateGreaterThan(form.getStartSolvedDate()))
				.and(solvedDateLessThan(form.getEndSolvedDate()))
				.and(contentFreeWordContains(form.getFreeWord())),
				PageRequest.of(pageNum, SEARCH_SIZE, Sort.by(
						form.getSortOrder().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
						form.getSortData())));
	}

	public Specification<InquiryRequirementComplaint> delFlgIsFalse() {
		return (root, query, cb) -> {
			if (Long.class != query.getResultType()) {
				query.distinct(true);
				root.fetch("client", JoinType.INNER);
				root.fetch("employee", JoinType.INNER);
			}
			return cb.isFalse(root.get("delFlg"));
		};
	}

	public Specification<InquiryRequirementComplaint> clientIdContains(List<Integer> clientIdList) {
		return CollectionUtils.isEmpty(clientIdList) ? null : (root, query, cb) -> {
			return root.get("clientId").in(clientIdList);
		};
	}

	public Specification<InquiryRequirementComplaint> contentTypesEquals(Integer contentType) {
		return contentType == null || contentType < 1 ? null : (root, query, cb) -> {
			return cb.equal(root.get("contentType"), contentType);
		};
	}

	public Specification<InquiryRequirementComplaint> occurredDateGreaterThan(LocalDate startDate) {
		return startDate == null ? null : (root, query, cb) -> {
			return cb.greaterThanOrEqualTo(root.get("occurredDate"), startDate);
		};
	}

	public Specification<InquiryRequirementComplaint> occurredDateLessThan(LocalDate endDate) {
		return endDate == null ? null : (root, query, cb) -> {
			return cb.lessThanOrEqualTo(root.get("occurredDate"), endDate);
		};
	}

	public Specification<InquiryRequirementComplaint> hasSolved(Boolean hasSolved) {
		return hasSolved == null ? null : (root, query, cb) -> {
			if (hasSolved) {
				return cb.isTrue(root.get("hasSolved"));
			} else {
				return cb.isFalse(root.get("hasSolved"));
			}
		};
	}

	public Specification<InquiryRequirementComplaint> responsibleEmployeeIdContains(List<Integer> responsibleEmployeeIdList) {
		return CollectionUtils.isEmpty(responsibleEmployeeIdList) ? null : (root, query, cb) -> {
			return root.get("responsibleEmployeeId").in(responsibleEmployeeIdList);
		};
	}

	public Specification<InquiryRequirementComplaint> solvedDateGreaterThan(LocalDate startDate) {
		return startDate == null ? null : (root, query, cb) -> {
			return cb.greaterThanOrEqualTo(root.get("solvedDate"), startDate);
		};
	}

	public Specification<InquiryRequirementComplaint> solvedDateLessThan(LocalDate endDate) {
		return endDate == null ? null : (root, query, cb) -> {
			return cb.lessThanOrEqualTo(root.get("solvedDate"), endDate);
		};
	}

	public Specification<InquiryRequirementComplaint> contentFreeWordContains(String freeWord) {
		return StringUtils.isBlank(freeWord) ? null : (root, query, cb) -> {
			return cb.or(cb.like(root.get("content"), "%" + freeWord + "%"),
					cb.like(root.get("solvedContent"), "%" + freeWord + "%"));
		};
	}

	private InquiryRequirementComplaint form2Model(InquiryRequirementComplaintForm form) {

		InquiryRequirementComplaint inquiry = new InquiryRequirementComplaint();

		if (form.getId() != null) {
			inquiry = inquiryRequirementComplaintRepository.findByInquiryRequirementComplaintId(form.getId());
		}

		inquiry.setClientId(form.getClientId());
		Client client = clientRepository.findByClientId(form.getClientId());
		inquiry.setClient(client);

		inquiry.setContentType(form.getContentType());
		inquiry.setContent(form.getContent());
		inquiry.setOccurredDate(form.getOccurredDate());
		inquiry.setResponsibleEmployeeId(form.getResponsibleEmployeeId());
		inquiry.setHasSolved(form.getHasSolved());
		if (form.getHasSolved()) {
			inquiry.setSolvedDate(form.getSolvedDate());
		} else {
			inquiry.setSolvedDate(null);
		}
		inquiry.setSolvedContent(form.getSolvedContent());
		inquiry.setDelFlg(false);
		inquiry = inquiryRequirementComplaintRepository.save(inquiry);

		return inquiry;
	}

}

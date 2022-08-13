package com.workbench.kato_system.admin.business_card.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.workbench.kato_system.admin.business_card.form.BusinessCardForm;
import com.workbench.kato_system.admin.business_card.form.BusinessCardSearchForm;
import com.workbench.kato_system.admin.business_card.model.BusinessCard;
import com.workbench.kato_system.admin.business_card.repository.BusinessCardRepository;
import com.workbench.kato_system.admin.client.model.entity.Client;
import com.workbench.kato_system.admin.client.model.entity.ClientEmployee;
import com.workbench.kato_system.admin.client.repository.ClientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessCardService {

	private final BusinessCardRepository businessCardRepository;
	private final ClientRepository clientRepository;

	private final int SEARCH_SIZE = 10;

	public List<BusinessCard> getAll() {
		return businessCardRepository.findAll();
	}

	public Page<BusinessCard> getPageList(Pageable pageable) {
		if (pageable == null) {
			return null;
		}
		return businessCardRepository.findAll(pageable);
	}

	public BusinessCard getOne(Integer id) {
		Optional<BusinessCard> bc = businessCardRepository.findById(id);
		return bc.orElse(new BusinessCard());
	}

	public BusinessCard save(BusinessCardForm form) throws IOException {

		BusinessCard businessCard = form2BusinessCard(form);

		return businessCard;
	}

	public void delete(Integer id) {

		Optional<BusinessCard> bc = businessCardRepository.findById(id);
		if (bc.isPresent()) {
			BusinessCard businessCard = bc.get();
			businessCard.setDelFlg(true);
			businessCardRepository.save(businessCard);
		}
	}

	public Page<BusinessCard> getSearchResult(Integer pageNum, BusinessCardSearchForm form) {
		return businessCardRepository.findAll(Specification
				.where(delFlgIsFalse())
				.and(companyNameContains(form.getCompanyNameList()))
				.and(nameContains(form.getTargetName()))
				.and(dateGreaterThan(form.getStartExchangeDate()))
				.and(dateLessThan(form.getEndExchangeDate())),
				PageRequest.of(pageNum, SEARCH_SIZE, Sort.by(Sort.Direction.ASC, "id")));
	}

	public Specification<BusinessCard> delFlgIsFalse() {
		return (root, query, cb) -> {
			query.distinct(true);
			return cb.isFalse(root.get("delFlg"));
		};
	}

	public Specification<BusinessCard> companyNameContains(List<String> companyNameList) {
		return CollectionUtils.isEmpty(companyNameList) ? null : (root, query, cb) -> {
			query.distinct(true);
			return root.get("companyName").in(companyNameList);
		};
	}

	public Specification<BusinessCard> nameContains(String name) {
		return StringUtils.isBlank(name) ? null : (root, query, cb) -> {
			query.distinct(true);
			return cb.like(root.get("name"), "%" + name + "%");
		};
	}

	public Specification<BusinessCard> dateGreaterThan(LocalDate startDate) {
		return startDate == null ? null : (root, query, cb) -> {
			query.distinct(true);
			return cb.greaterThanOrEqualTo(root.get("exchangeDate").as(LocalDate.class), startDate);
		};
	}

	public Specification<BusinessCard> dateLessThan(LocalDate endDate) {
		return endDate == null ? null : (root, query, cb) -> {
			query.distinct(true);
			return cb.lessThanOrEqualTo(root.get("exchangeDate").as(LocalDate.class), endDate);
		};
	}

	private BusinessCard form2BusinessCard(BusinessCardForm form) throws IOException {

		BusinessCard bc = new BusinessCard();

		if (form.getId() != null) {
			bc = businessCardRepository.findById(form.getId()).orElse(new BusinessCard());
		}

		List<Client> client = clientRepository.findByName(form.getCompanyName());

		if (!CollectionUtils.isEmpty(client)) {
			Client c = client.get(0);
			bc.setClientId(c.getId());
			bc.setClient(c);

			Set<ClientEmployee> clientEmployee = c.getClientEmployeeList();
			for (ClientEmployee ce : clientEmployee) {
				if (ce.getName().equals(form.getName())) {
					bc.setClientEmployeeId(ce.getId());
					bc.setClientEmployee(ce);
					break;
				}
			}
		}

		bc.setCompanyName(form.getCompanyName());
		bc.setName(form.getName());
		bc.setDepartment(form.getDepartment());
		bc.setPosition(form.getPosition());
		bc.setTel(form.getTel());
		bc.setEmail(form.getEmail());
		bc.setExchangeDate(form.getExchangeDate());
		if (form.getImage() != null && StringUtils.isNotBlank(form.getImage().getOriginalFilename())) {
			bc.setFileName(form.getImage().getOriginalFilename());
			bc.setContentType(form.getImage().getContentType());
			bc.setImage(form.getImage().getBytes());
		}
		bc.setDelFlg(false);
		bc = businessCardRepository.save(bc);

		return bc;
	}

}

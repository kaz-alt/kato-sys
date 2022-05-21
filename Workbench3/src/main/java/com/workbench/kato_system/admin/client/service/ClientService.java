package com.workbench.kato_system.admin.client.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.JoinType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.workbench.kato_system.admin.client.dto.ClientDto;
import com.workbench.kato_system.admin.client.dto.ClientStaffDto;
import com.workbench.kato_system.admin.client.form.ClientForm;
import com.workbench.kato_system.admin.client.form.ClientSearchForm;
import com.workbench.kato_system.admin.client.form.ClientStaffForm;
import com.workbench.kato_system.admin.client.model.entity.Client;
import com.workbench.kato_system.admin.client.model.entity.ClientStaff;
import com.workbench.kato_system.admin.client.repository.ClientRepository;
import com.workbench.kato_system.admin.client.repository.ClientStaffRepository;
import com.workbench.kato_system.admin.security.LoginUserDetails;
import com.workbench.kato_system.admin.staff.model.Staff;
import com.workbench.kato_system.admin.staff.model.StaffClient;
import com.workbench.kato_system.admin.staff.repository.StaffClientRepository;
import com.workbench.kato_system.admin.staff.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

	private final ClientRepository clientRepository;
	private final ClientStaffRepository clientStaffRepository;
	private final StaffRepository staffRepository;
	private final StaffClientRepository staffClientRepository;

	private final int SEARCH_SIZE = 10;

	public List<Client> getAll() {
		return clientRepository.findAll();
	}

	public Page<Client> getPageList(Pageable pageable) {
		return clientRepository.findAll(pageable);
	}

	public Client getOne(Integer id) {
		return clientRepository.findByClientId(id);
	}

	public List<String> getClienNameListtByName(String name) {
		return clientRepository.findNameListByName(name);
	}

	public List<Client> getClientListtByName(String name) {
		return clientRepository.findListByName(name);
	}

	public Map<Integer, Integer> getProjectCount() {
		List<Client> clients = clientRepository.findClientsWithProject();

		Map<Integer, Integer> map = new HashMap<>();

		for (Client client : clients) {
			if (map.get(client.getId()) == null) {
				map.put(client.getId(), 1);
			} else {
				int count = map.get(client.getId());
				count += 1;
				map.put(client.getId(), count);
			}
		}

		return map;
	}

	public Client save(ClientForm form, LoginUserDetails user) {

		Client client = form2Client(form, user);

		return client;
	}

	public void delete(Integer id) {
		// 論理削除
		Client client = clientRepository.findByClientId(id);
		client.setDelFlg(true);
		clientRepository.save(client);
	}

	public List<ClientDto> getClientDtoByName(String name) {
		List<Client> clientList = clientRepository.findListByName(name);
		return model2Dto(clientList);
	}

	public List<ClientStaffDto> getClientStaffDtoByClientId(Integer clientId) {
		List<ClientStaff> clientStaffList = clientStaffRepository.findByClientId(clientId);
		return model2ClientStaffDto(clientStaffList);
	}

	public Page<Client> getSearchResult(Integer pageNum, ClientSearchForm form) {
		return clientRepository.findAll(Specification
				.where(createJoinFetch())
				.and(delFlgIsFalse())
				.and(nameContains(form.getTargetClientNameList()))
				.and(industryIdContains(form.getIndustryIdList()))
				.and(typeIdContains(form.getClientTypeIdList()))
				.and(staffIdContains(form.getOurStaffIdList()))
				.and(telContains(form.getClientTel()))
				.and(emailContains(form.getClientEmail())),
				PageRequest.of(pageNum, SEARCH_SIZE,
						Sort.by(form.getSortOrder().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
								form.getSortData())));
	}

	// 全ての検索結果にFetchを効かせるために盛り込む
	// FetchがないとN+1が発生する
	public Specification<Client> createJoinFetch() {
		return (root, query, cb) -> {
			if (Long.class != query.getResultType()) {
				query.distinct(true);
				root.fetch("staffClientList", JoinType.INNER).fetch("staff", JoinType.INNER);
				root.fetch("clientStaffList", JoinType.INNER);
			}
			return cb.conjunction();
		};
	}

	public Specification<Client> delFlgIsFalse() {
		return (root, query, cb) -> {
			return cb.isFalse(root.get("delFlg"));
		};
	}

	public Specification<Client> nameContains(List<String> nameList) {
		return CollectionUtils.isEmpty(nameList) ? null : (root, query, cb) -> {
			return root.get("name").in(nameList);
		};
	}

	public Specification<Client> industryIdContains(List<Integer> industryIdList) {
		return CollectionUtils.isEmpty(industryIdList) ? null : (root, query, cb) -> {
			return root.get("industry").in(industryIdList);
		};
	}

	public Specification<Client> typeIdContains(List<Integer> typeIdList) {
		return CollectionUtils.isEmpty(typeIdList) ? null : (root, query, cb) -> {
			return root.get("clientType").in(typeIdList);
		};
	}

	public Specification<Client> staffIdContains(List<Integer> staffIdList) {
		return CollectionUtils.isEmpty(staffIdList) ? null : (root, query, cb) -> {
			return root.join("staffClientList", JoinType.INNER).get("staffId").in(staffIdList);
		};
	}

	public Specification<Client> telContains(String tel) {
		return !StringUtils.hasText(tel) ? null : (root, query, cb) -> {
			return cb.like(root.join("clientStaffList", JoinType.LEFT).get("tel"), "%" + tel + "%");
		};
	}

	public Specification<Client> emailContains(String email) {
		return !StringUtils.hasText(email) ? null : (root, query, cb) -> {
			return cb.like(root.join("clientStaffList", JoinType.LEFT).get("email"), "%" + email + "%");
		};
	}

	private Client form2Client(ClientForm form, LoginUserDetails user) {

		Client client = new Client();

		boolean isNew = form.getId() == null;

		if (!isNew) {
			client = clientRepository.findByClientId(form.getId());
		}

		client.setName(form.getClientName());
		client.setIndustry(form.getIndustryId());
		client.setClientType(form.getClientTypeId());
		client.setAddress(form.getAddress());
		client.setUrl(form.getUrl());
		client.setFirstInterviewDate(form.getInterviewDate());
		client.setDelFlg(false);
		client = clientRepository.save(client);

		LocalDateTime now = LocalDateTime.now();
		// 顧客担当者を登録
		Set<ClientStaff> clientStaffList = form2clientStaff(form, client, user, now);
		// 当社担当者を登録
		Set<StaffClient> staffClinetList = form2StaffClient(form, client, user, now);

		if (!isNew) {
			staffClientRepository.deleteByClientId(client.getId());
			clientStaffRepository.deleteByClientId(client.getId());
		}

		client.setClientStaffList(clientStaffList);
		client.setStaffClientList(staffClinetList);
		clientRepository.save(client);

		return client;

	}

	private Set<ClientStaff> form2clientStaff(ClientForm form, Client client, LoginUserDetails user,
			LocalDateTime now) {

		Set<ClientStaff> ClientStaffList = new HashSet<>();

		List<ClientStaffForm> clientStaffFormList = form.getClientStaff();

		for (ClientStaffForm cs : clientStaffFormList) {
			ClientStaff clientStaff = new ClientStaff();
			clientStaff.setName(cs.getName());
			clientStaff.setDepartment(cs.getDepartment());
			clientStaff.setPosition(cs.getPosition());
			clientStaff.setTel(cs.getTel());
			clientStaff.setEmail(cs.getEmail());
			clientStaff.setStandpoint(cs.getStandpoint());
			clientStaff.setMotivation(cs.getMotivation());
			clientStaff.setRemarks(cs.getRemarks());
			clientStaff.setClient(client);
			clientStaff.setClientId(client.getId());
			clientStaff.setCreatedBy(user.getUsername());
			clientStaff.setCreatedDate(now);
			ClientStaffList.add(clientStaff);
		}

		return ClientStaffList;
	}

	private Set<StaffClient> form2StaffClient(ClientForm form, Client client, LoginUserDetails user,
			LocalDateTime now) {

		Set<StaffClient> staffClientList = new HashSet<>();

		List<Staff> staffList = staffRepository.findByIdIn(form.getStaffIdList());

		for (Staff staff : staffList) {
			StaffClient staffClient = new StaffClient();
			staffClient.setStaffId(staff.getId());
			staffClient.setStaff(staff);
			staffClient.setClient(client);
			staffClient.setClientId(client.getId());
			staffClient.setCreatedBy(user.getUsername());
			staffClient.setCreatedDate(now);
			staffClientList.add(staffClient);
		}

		return staffClientList;
	}

	public List<ClientDto> getSelectedClient(List<Integer> clientIdList) {
		List<Client> clientList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(clientIdList)) {
			clientList = clientRepository.findByIdIn(clientIdList);
		}
		return model2Dto(clientList);
	}

	private List<ClientDto> model2Dto(List<Client> clientList) {
		List<ClientDto> dtoList = new ArrayList<>();

		if (CollectionUtils.isEmpty(clientList)) {
			return dtoList;
		}

		for (Client client : clientList) {
			ClientDto dto = new ClientDto();
			dto.setId(client.getId());
			dto.setName(client.getName());
			dtoList.add(dto);
		}

		return dtoList;
	}

	private List<ClientStaffDto> model2ClientStaffDto(List<ClientStaff> clientStaffList) {
		List<ClientStaffDto> dtoList = new ArrayList<>();

		if (CollectionUtils.isEmpty(clientStaffList)) {
			return dtoList;
		}

		for (ClientStaff cs : clientStaffList) {
			ClientStaffDto dto = new ClientStaffDto();
			dto.setId(cs.getId());
			dto.setName(cs.getName());
			dtoList.add(dto);
		}

		return dtoList;
	}
}

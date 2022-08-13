package com.workbench.kato_system.admin.client.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.JoinType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.workbench.kato_system.admin.client.dto.ClientDto;
import com.workbench.kato_system.admin.client.dto.ClientEmployeeDto;
import com.workbench.kato_system.admin.client.form.ClientForm;
import com.workbench.kato_system.admin.client.form.ClientSearchForm;
import com.workbench.kato_system.admin.client.form.ClientEmployeeForm;
import com.workbench.kato_system.admin.client.model.entity.Client;
import com.workbench.kato_system.admin.client.model.entity.ClientEmployee;
import com.workbench.kato_system.admin.client.repository.ClientRepository;
import com.workbench.kato_system.admin.client.repository.ClientEmployeeRepository;
import com.workbench.kato_system.admin.employee.model.Employee;
import com.workbench.kato_system.admin.employee.model.EmployeeClient;
import com.workbench.kato_system.admin.employee.repository.EmployeeClientRepository;
import com.workbench.kato_system.admin.employee.repository.EmployeeRepository;
import com.workbench.kato_system.admin.login.model.LoginUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

	private final ClientRepository clientRepository;
	private final ClientEmployeeRepository clientEmployeeRepository;
	private final EmployeeRepository employeeRepository;
	private final EmployeeClientRepository employeeClientRepository;

	private final int SEARCH_SIZE = 10;
	private final String CLINET_EMPLOYEE_LIST = "clientEmployeeList";

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

	public List<ClientEmployeeDto> getClientEmployeeDtoByClientId(Integer clientId) {
		List<ClientEmployee> clientEmployeeList = clientEmployeeRepository.findByClientId(clientId);
		return model2ClientEmployeeDto(clientEmployeeList);
	}

	public Page<Client> getSearchResult(Integer pageNum, ClientSearchForm form) {
		return clientRepository.findAll(Specification
				.where(createJoinFetch())
				.and(delFlgIsFalse())
				.and(nameContains(form.getTargetClientNameList()))
				.and(industryIdContains(form.getIndustryIdList()))
				.and(typeIdContains(form.getClientTypeIdList()))
				.and(employeeIdContains(form.getOurEmployeeIdList()))
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
				root.fetch("employeeClientList", JoinType.INNER).fetch("employee", JoinType.INNER);
				root.fetch(CLINET_EMPLOYEE_LIST, JoinType.INNER);
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

	public Specification<Client> employeeIdContains(List<Integer> employeeIdList) {
		return CollectionUtils.isEmpty(employeeIdList) ? null : (root, query, cb) -> {
			return root.join("employeeClientList", JoinType.INNER).get("employeeId").in(employeeIdList);
		};
	}

	public Specification<Client> telContains(String tel) {
		return !StringUtils.isBlank(tel) ? null : (root, query, cb) -> {
			return cb.like(root.join(CLINET_EMPLOYEE_LIST, JoinType.LEFT).get("tel"), "%" + tel + "%");
		};
	}

	public Specification<Client> emailContains(String email) {
		return !StringUtils.isBlank(email) ? null : (root, query, cb) -> {
			return cb.like(root.join(CLINET_EMPLOYEE_LIST, JoinType.LEFT).get("email"), "%" + email + "%");
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
		Set<ClientEmployee> clientEmployeeList = form2clientEmployee(form, client, user, now);
		// 当社担当者を登録
		Set<EmployeeClient> employeeClinetList = form2EmployeeClient(form, client, user, now);

		if (!isNew) {
			employeeClientRepository.deleteByClientId(client.getId());
			clientEmployeeRepository.deleteByClientId(client.getId());
		}

		client.setClientEmployeeList(clientEmployeeList);
		client.setEmployeeClientList(employeeClinetList);
		clientRepository.save(client);

		return client;

	}

	private Set<ClientEmployee> form2clientEmployee(ClientForm form, Client client, LoginUserDetails user,
			LocalDateTime now) {

		Set<ClientEmployee> ClientEmployeeSet = new HashSet<>();

		List<ClientEmployeeForm> clientEmployeeFormList = form.getClientEmployee();

		for (ClientEmployeeForm cs : clientEmployeeFormList) {
			ClientEmployee clientEmployee = new ClientEmployee();
			clientEmployee.setName(cs.getName());
			clientEmployee.setDepartment(cs.getDepartment());
			clientEmployee.setPosition(cs.getPosition());
			clientEmployee.setTel(cs.getTel());
			clientEmployee.setEmail(cs.getEmail());
			clientEmployee.setStandpoint(cs.getStandpoint());
			clientEmployee.setMotivation(cs.getMotivation());
			clientEmployee.setRemarks(cs.getRemarks());
			clientEmployee.setClient(client);
			clientEmployee.setClientId(client.getId());
			clientEmployee.setCreatedBy(user.getUsername());
			clientEmployee.setCreatedDate(now);
			ClientEmployeeSet.add(clientEmployee);
		}

		return ClientEmployeeSet;
	}

	private Set<EmployeeClient> form2EmployeeClient(ClientForm form, Client client, LoginUserDetails user,
			LocalDateTime now) {

		Set<EmployeeClient> employeeClientList = new HashSet<>();

		List<Employee> employeeList = employeeRepository.findByIdIn(form.getEmployeeIdList());

		for (Employee employee : employeeList) {
			EmployeeClient employeeClient = new EmployeeClient();
			employeeClient.setEmployeeId(employee.getId());
			employeeClient.setEmployee(employee);
			employeeClient.setClient(client);
			employeeClient.setClientId(client.getId());
			employeeClient.setCreatedBy(user.getUsername());
			employeeClient.setCreatedDate(now);
			employeeClientList.add(employeeClient);
		}

		return employeeClientList;
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

	private List<ClientEmployeeDto> model2ClientEmployeeDto(List<ClientEmployee> clientEmployeeList) {
		List<ClientEmployeeDto> dtoList = new ArrayList<>();

		if (CollectionUtils.isEmpty(clientEmployeeList)) {
			return dtoList;
		}

		for (ClientEmployee cs : clientEmployeeList) {
			ClientEmployeeDto dto = new ClientEmployeeDto();
			dto.setId(cs.getId());
			dto.setName(cs.getName());
			dtoList.add(dto);
		}

		return dtoList;
	}
}

package com.workbench.kato_system.admin.product.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.JoinType;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.workbench.kato_system.admin.client.dto.ClientDto;
import com.workbench.kato_system.admin.client.model.entity.Client;
import com.workbench.kato_system.admin.client.repository.ClientRepository;
import com.workbench.kato_system.admin.product.dto.ProductDto;
import com.workbench.kato_system.admin.product.form.ProductForm;
import com.workbench.kato_system.admin.product.form.ProductSearchForm;
import com.workbench.kato_system.admin.product.model.Product;
import com.workbench.kato_system.admin.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ClientRepository clientRepository;
	private final ProductRepository productRepository;

	private final int SEARCH_SIZE = 10;

	public List<Product> getAll() {
		return productRepository.findAll();
	}

	public Page<Product> getPageList(Pageable pageable) {
		if (pageable == null) {
			return null;
		}
		return productRepository.findAll(pageable);
	}

	public Product getOne(Integer id) {
		return productRepository.findByProductId(id);
	}

	public List<Product> getListByClientIdAndPurchasedDate(Integer clientId, LocalDate startDate,
			LocalDate endDate) {

		return productRepository.findByClientIdAndpurchasedDate(clientId, startDate, endDate);

	}

	public List<ProductDto> getDtoList(List<Product> list) {

		return model2Dto(list);
	}

	public Product save(ProductForm form) {

		Product product = form2Product(form);

		return product;
	}

	public void delete(Integer id) {

		Product product = productRepository.findByProductId(id);
		productRepository.delete(product);
	}

	public Page<Product> getSearchResult(Integer pageNum, ProductSearchForm form) {
		return productRepository.findAll(Specification
				.where(delFlgIsFalse())
				.and(clientIdContains(form.getClientIdList()))
				.and(nameContains(form.getTargetProductName()))
				.and(salesGreaterThan(form.getStartProductSales()))
				.and(salesLessThan(form.getEndProductSales()))
				.and(dateGreaterThan(form.getStartPurchasedDate()))
				.and(dateLessThan(form.getEndPurchasedDate())),
				PageRequest.of(pageNum, SEARCH_SIZE, Sort.by(
						form.getSortOrder().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
						form.getSortData())));
	}

	public Specification<Product> delFlgIsFalse() {
		return (root, query, cb) -> {
			if (Long.class != query.getResultType()) {
				query.distinct(true);
				root.fetch("client", JoinType.INNER);
			}
			return cb.isFalse(root.get("delFlg"));
		};
	}

	public Specification<Product> clientIdContains(List<Integer> clientIdList) {
		return CollectionUtils.isEmpty(clientIdList) ? null : (root, query, cb) -> {
			query.distinct(true);
			return root.get("clientId").in(clientIdList);
		};
	}

	public Specification<Product> nameContains(String name) {
		return StringUtils.isEmpty(name) ? null : (root, query, cb) -> {
			query.distinct(true);
			return cb.like(root.get("name"), "%" + name + "%");
		};
	}

	public Specification<Product> salesGreaterThan(Integer startSales) {
		return startSales == null ? null : (root, query, cb) -> {
			query.distinct(true);
			return cb.greaterThanOrEqualTo(root.get("sales"), startSales);
		};
	}

	public Specification<Product> salesLessThan(Integer endSales) {
		return endSales == null ? null : (root, query, cb) -> {
			query.distinct(true);
			return cb.lessThanOrEqualTo(root.get("sales"), endSales);
		};
	}

	public Specification<Product> dateGreaterThan(LocalDate startDate) {
		return startDate == null ? null : (root, query, cb) -> {
			query.distinct(true);
			return cb.greaterThanOrEqualTo(root.get("purchasedDate"), startDate);
		};
	}

	public Specification<Product> dateLessThan(LocalDate endDate) {
		return endDate == null ? null : (root, query, cb) -> {
			query.distinct(true);
			return cb.lessThanOrEqualTo(root.get("purchasedDate"), endDate);
		};
	}

	private Product form2Product(ProductForm form) {

		Product product = new Product();

		if (form.getId() != null) {
			product = productRepository.findByProductId(form.getId());
		}

		product.setClientId(form.getClientId());
		Client client = clientRepository.findByClientId(form.getClientId());
		product.setClient(client);

		product.setName(form.getProductName());
		product.setQuantity(form.getProductQuantity());
		product.setSales(form.getProductSales());
		product.setPurchasedDate(form.getPurchasedDate());
		product.setRemarks(form.getProductRemarks());
		product.setDelFlg(false);
		product = productRepository.save(product);

		return product;
	}

	private List<ProductDto> model2Dto(List<Product> list) {

		List<ProductDto> dtoList = new ArrayList<>();

		for (Product p : list) {

			ProductDto dto = new ProductDto();

			dto.setName(p.getName());
			dto.setQuantity(p.getQuantity());
			dto.setSales(p.getSales());
			dto.setPurchasedDate(String.valueOf(p.getPurchasedDate()));
			dto.setRemarks(p.getRemarks());

			dtoList.add(dto);
		}

		return dtoList;

	}

	private Set<ClientDto> model2ClientDto(List<Product> productList) {
		Set<ClientDto> dtoSet = new HashSet<>();

		if (CollectionUtils.isEmpty(productList)) {
			return dtoSet;
		}

		for (Product prduct : productList) {
			ClientDto dto = new ClientDto();
			dto.setId(prduct.getClient().getId());
			dto.setName(prduct.getClient().getName());
			dtoSet.add(dto);
		}

		return dtoSet;
	}
}

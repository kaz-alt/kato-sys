package com.workbench.kato_system.admin.product.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

	@Override
	@Query("select distinct p from Product p inner join fetch p.client c order by p.purchasedDate")
	List<Product> findAll();

	@Override
	@Query(value = "select distinct p from Product p "
			+ "inner join fetch p.client c "
			+ "where p.delFlg = 0 "
			+ "order by p.clientId, p.purchasedDate", countQuery = "select count(distinct p) from Product p "
					+ "inner join p.client c "
					+ "where p.delFlg = 0 "
					+ "order by p.clientId, p.purchasedDate")
	Page<Product> findAll(Pageable pageable);

	@Query("select distinct p from Product p "
			+ "inner join fetch p.client c "
			+ "where p.id = :id "
			+ "and p.delFlg = 0")
	Product findByProductId(@Param("id") Integer id);

	@Query("select distinct p from Product p "
			+ "inner join fetch p.client c "
			+ "where p.delFlg = 0 "
			+ "and p.clientId = :clientId "
			+ "and p.purchasedDate >= :startDate "
			+ "and p.purchasedDate <= :endDate "
			+ "order by p.purchasedDate")
	List<Product> findByClientIdAndpurchasedDate(@Param("clientId") Integer clientId,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	List<Product> findByClientIdIn(List<Integer> clientIdList);

}

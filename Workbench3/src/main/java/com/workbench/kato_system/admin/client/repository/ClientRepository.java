package com.workbench.kato_system.admin.client.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.client.model.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Integer>, JpaSpecificationExecutor<Client> {

	@Override
	@Query("select distinct c from Client c "
			+ "left join fetch c.staffClientList "
			+ "left join fetch c.clientStaffList "
			+ "order by c.id")
	List<Client> findAll();

	@Override
	@Query(value = "select distinct c from Client c "
			+ "left join fetch c.staffClientList sc "
			+ "left join fetch sc.staff "
			+ "left join fetch c.clientStaffList "
			+ "where c.delFlg = 0", countQuery = "select count(distinct c) from Client c "
					+ "left join c.staffClientList sc "
					+ "left join sc.staff "
					+ "left join c.clientStaffList "
					+ "where c.delFlg = 0"
					+ "order by c.id")
	Page<Client> findAll(Pageable pageable);

	@Query("select distinct c from Client c "
			+ "left join fetch c.staffClientList sc "
			+ "left join fetch sc.staff "
			+ "left join fetch c.clientStaffList "
			+ "where c.id = :id "
			+ "and c.delFlg = 0")
	Client findByClientId(@Param("id") Integer id);

	@Query("select c.name from Client c where c.name like %:name% and c.delFlg = 0")
	List<String> findNameListByName(@Param("name") String name);

	@Query("select c from Client c where c.name like %:name% and c.delFlg = 0")
	List<Client> findListByName(@Param("name") String name);

	@Query("select c from Client c where c.name = :name and c.delFlg = 0")
	List<Client> findByName(@Param("name") String name);

	List<Client> findByIdIn(List<Integer> clientIdList);

	@Query("select c from Client c "
			+ "inner join Project p "
			+ "on c.id = p.clientId "
			+ "where c.delFlg = 0 "
			+ "and p.delFlg = 0")
	List<Client> findClientsWithProject();

}

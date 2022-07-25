package com.workbench.kato_system.admin.project.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workbench.kato_system.admin.analysis.dto.AnalysisDto;
import com.workbench.kato_system.admin.project.dto.ProjectDto;
import com.workbench.kato_system.admin.project.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer>, JpaSpecificationExecutor<Project> {

  @Query("SELECT p FROM Project p "
    + "INNER JOIN FETCH p.progress "
    + "INNER JOIN FETCH p.client "
    + "INNER JOIN FETCH p.approachRoot "
    + "LEFT JOIN FETCH p.factor "
    + "LEFT JOIN FETCH p.projectEmployee pe "
    + "LEFT JOIN FETCH pe.employee "
    + "LEFT JOIN FETCH p.activity a "
    + "LEFT JOIN FETCH a.employee "
    + "WHERE p.id = :id")
  Optional<Project> findByProjectId(@Param("id") Integer id);

	@Override
	@Query(value = "select p from Project p "
    + "inner join fetch p.progress "
    + "inner join fetch p.client c "
    + "inner join fetch p.approachRoot "
    + "left join fetch p.factor "
    + "left join fetch p.projectEmployee pe "
    + "left join fetch pe.employee "
    + "where p.delFlg = 0 "
    + "order by c.name ", countQuery = "select p from Project p "
      + "left join p.factor "
      + "left join p.projectEmployee pe "
      + "left join pe.employee "
      + "where p.delFlg = 0")
	Page<Project> findAll(Pageable pagable);
	@Query(value = "select p from Project p "
			+ "inner join fetch p.progress "
			+ "inner join fetch p.approachRoot "
			+ "left join fetch p.factor "
			+ "left join fetch p.client "
			+ "left join fetch p.projectEmployee pe "
			+ "left join fetch pe.employee "
			+ "where p.delFlg = 0 "
			+ "and p.clientId = :clientId "
			+ "and p.expectedOrderDate >= :startDate "
			+ "and p.expectedOrderDate <= :endDate")
	Set<Project> findByClientIdAndExpectedOrderDate(@Param("clientId") Integer clientId,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
  @Query("SELECT new com.workbench.kato_system.admin.project.dto.ProjectDto(p.id, p.name) FROM Project p "
    + "WHERE p.delFlg = 0 AND p.name LIKE %:name%")
  List<ProjectDto> fetchDtoByName(@Param("name") String name);

List<Project> findByIdIn(List<Integer> idList);

@Query("select "
  + "new com.workbench.kato_system.admin.analysis.dto.AnalysisDto("
  +   "p.clientId, c.name, max(p.expectedOrderDate), count(p.estimatedOrderAmount), sum(p.estimatedOrderAmount)) "
  + "from Project p "
  + "inner join p.client c "
  + "group by p.clientId")
  List<AnalysisDto> fetchAnalysisDto();

}

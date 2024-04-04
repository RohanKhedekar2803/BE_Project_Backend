package com.example.BE_PROJECT_OPEN_COLLAB.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.Challenges;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.Repositor;

@Repository
public interface ChallengesRepository extends
PagingAndSortingRepository<Challenges, Long>,CrudRepository<Challenges, Long>{
	
	Page findAll(Pageable paging);
	
	Page<Challenges> findAll(Specification<Challenges> spec, Pageable pageable);
	
	List<Challenges> findAll(Specification<Challenges> spec);

	List<Challenges> findByCreatedBy(String username);
	
	
}

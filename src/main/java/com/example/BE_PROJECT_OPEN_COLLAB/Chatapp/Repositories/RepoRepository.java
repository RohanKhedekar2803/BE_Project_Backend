package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.Repositor;
import java.util.List;


@Repository
public interface RepoRepository
		extends PagingAndSortingRepository<Repositor, String>, CrudRepository<Repositor, String> {

	Repositor save(Repositor repo);
	
	Repositor findById(Long id);

	Page findAll(Pageable paging);

	Page<Repositor> findAll(Specification<Repositor> spec, Pageable paging);
	
	List<Repositor> findAll(Specification<Repositor> spec);
	
	List<Repositor> findAll();

}

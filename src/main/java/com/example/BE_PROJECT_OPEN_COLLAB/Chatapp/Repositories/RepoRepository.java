package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.Repositor;

@Repository
public interface RepoRepository
		extends PagingAndSortingRepository<Repositor, String>, CrudRepository<Repositor, String> {

	Repositor save(Repositor repo);

	Page findAll(Pageable paging);

	Page<Repositor> findAll(Specification<Repositor> spec, Pageable paging);

}

package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.BE_PROJECT_OPEN_COLLAB.CustomException;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.Repositor;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.RepoRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.UserRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.FilterRepos;

@Service
public class RepositoryService {

	@Autowired
	private RepoRepository repoRepository;

	public Repositor save(Repositor repo) {
		System.out.println(repoRepository.save(repo));
		try {
			return repoRepository.save(repo);
		} catch (Exception e) {
			return null;
		}
	}

	public List<Repositor> getRepos(Integer pageNo, Integer pageSize, String sortBy, FilterRepos filterRepos) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Specification<Repositor> spec = Specification.where(null);

		if (!filterRepos.getHasLanguage().isEmpty()) {

			spec = spec.and(withHasLanguage(filterRepos.getHasLanguage()));
		} else if (!filterRepos.getHasTopic().isEmpty()) {

			spec = spec.and(withTopicContaining(filterRepos.getHasTopic()));
		}

		Page<Repositor> pagedResult = repoRepository.findAll(spec, paging);

		System.out.println("Data is --> " + pagedResult.getContent());

		return pagedResult.getContent();
	}
	
	public Repositor getRepoById(Long id) throws Exception{
		
			Repositor repository =repoRepository.findById(id);
			
			if(repository==null)throw new CustomException("repository with "+ id + " doesn't exist");
			
			return repository;	
	}

	static Specification<Repositor> withHasLanguage(String hasLanguage) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("language"), hasLanguage);
	}

	static Specification<Repositor> withTopicContaining(String topic) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("topics"), "%'" + topic + "'%");
	}


}

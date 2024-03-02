package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.Repositor;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services.RepositoryService;
import com.example.BE_PROJECT_OPEN_COLLAB.Models.*;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.DateTimeUtils;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.FilterRepos;

import lombok.Builder;

@RestController
@RequestMapping("/repo")
public class RepositoryController {

	@Autowired
	private RepositoryService repositoryService;

	@GetMapping("/hi")
	public String greet() {
		return "heyyy";
	}

	@PostMapping("/save")
	public ResponseEntity<Repositor> addRepository(@RequestBody RepositoryRequest repo) {
		
		Repositor repository = Repositor.builder().name(repo.getName()).description(repo.description).url(repo.url)
				.createdAt(repo.getCreatedAt()).homepage(repo.getHomepage()).size(repo.getStars())
				.forks(repo.getForks()).stars(repo.getStars()).language(repo.getLanguage()).topics(repo.getTopics())
				.build();

		Repositor result = repositoryService.save(repository);

		if (result == null)
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<List<Repositor>> retriveRepositories(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
			@RequestBody FilterRepos filterRepos) {

		List<Repositor> result = repositoryService.getRepos(pageNo, pageSize, sortBy, filterRepos);

		if (result == null)
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@GetMapping("/{id}")
	public ResponseEntity<Repositor> getRepositoryById(@PathVariable("id") Long id) throws Exception{
		Repositor repo =repositoryService.getRepoById(id);
		return new ResponseEntity<Repositor>(repo,HttpStatus.OK);
	}
}

package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    //while sending data from fronend for topics send in "topics": "['aws', 'aws-sdk', 'go']", thsi format othrwqise filter wont work
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
	
	
	//-------------------------------------------------------------------------------------
	
	//NOTES- for filtering only 1 filter to be applied at a time either has topics or has language give
	// input space for user to enter any string and boolean to determine if want to sort by topic or lang 
	//NOTE- for sorting only 4 feilds must be allowed at frontend 
	@PostMapping("/getbyprofile/")
	public Page<Repositor> retriveRepositoriesByProfile(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "recommended") String sortBy,
			@RequestBody FilterRepos filterRepos,@RequestParam(defaultValue = "") String username) {

		//dummy data 
		String[] userLanguages = {"Java", "Python", "JavaScript"};
		String[] userTopics = {"Machine Learning", "Artificial Intelligence", "Cloud"};
		//
		
		Page<Repositor> result = repositoryService.getReposByProfile(pageNo, pageSize, sortBy, filterRepos, userLanguages, userTopics,username);

		return result;

	}
	
	//NOTE-exact matching NOT USED BY DEFAULT DONT MAKE FRONTEND CALL FROM IT 
	@PostMapping("/getbyprofilebyExactMatching/")
	public List<Repositor> retriveRepositoriesByProfileByBard(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "recommended") String sortBy,
			@RequestBody FilterRepos filterRepos,@RequestParam(defaultValue = "") String username) {

		//dummy data 
		String[] userLanguages = {"Java", "Python", "JavaScript"};
		String[] userTopics = {"Machine Learning", "Artificial Intelligence", "Cloud"};
		//
		
		
		List<Repositor> result = repositoryService.recommendRepositories(username);
		return result;

	}
}

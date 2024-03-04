package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.BE_PROJECT_OPEN_COLLAB.CustomException;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.Repositor;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.RepoRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.UserRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.CosineSimilarityAlgorithm.Demo;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.FilterRepos;

@Service
public class RepositoryService {

	@Autowired
	private RepoRepository repoRepository;
	
	@Autowired
	private UserServices userServices;
	
	@Autowired
	private Demo demo;

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
	
	
	
	//--------------------------------------------------------------------------------------
	
	/* http://{server}:{port}/repo/getbyprofile/
	 * http://{server}:{port}/repo/getbyprofile/?username=pranitk7&pageNo=0&pageSize=5&sortBy=SalaryPerYear
	 * by default based on users profile repos are served and sorting & filtering fdrom that recomended only
	 * if sorting is provided by user then use that 
	 * 
	 * case 1 
	 * */
	
	
	public Page<Repositor> getReposByProfile(Integer pageNo, Integer pageSize, String sortBy, FilterRepos filterRepos, String[] userLanguages, String[] userTopics, String username) {
	    Pageable paging;
	    
	    //get users languages and topics
	    if(!username.equals("")) {
	         userLanguages =userServices.getFavoriteLanguagesByUsernameFromDb(username);
	         userTopics =userServices.getFavoriteTopicsByUsernameFromDb(username);
	    }

	    System.out.println(username.length() + userTopics.length + "size of interests");
	    
	    // Fetch all repositories from the database
	    List<Repositor> allRepositories = repoRepository.findAll();

	    // Apply filtering if provided
	    Specification<Repositor> spec = Specification.where(null);
	    if (!filterRepos.getHasLanguage().isEmpty()) {
	        spec = spec.and(withHasLanguage(filterRepos.getHasLanguage()));
	    } else if (!filterRepos.getHasTopic().isEmpty()) {
	        spec = spec.and(withTopicContaining(filterRepos.getHasTopic()));
	    }

	    // Filter repositories based on the provided criteria
	    List<Repositor> filteredRepositories = repoRepository.findAll(spec);

	    // Check if custom sorting is applied
	    if ("recommended".equals(sortBy)) {
	        System.out.println("in recommendation");
	        // Extract languages and topics from the fetched repositories
	        List<List<String>> documents = extractLanguagesAndTopics(allRepositories);

	        // Sort repositories using TF-IDF algorithm based on user languages and topics
	        List<Integer> sortedIndices = demo.sortRepositories(userLanguages, userTopics, documents);

	        // Map the sorted indices to the paged result
	        List<Repositor> sortedRepositories = new ArrayList<>();
	        for (int index : sortedIndices) {
	            sortedRepositories.add(allRepositories.get(index));
	        }

	        // Apply filtering on the custom sorted data
	        sortedRepositories = applyFiltering(sortedRepositories, filterRepos);

	        // Create a new page with the sorted and filtered repositories
	        return paginate(sortedRepositories, pageNo, pageSize);
	    } else {
	        System.out.println("not in recommendation");
	        // Apply sorting as specified by Sort.by
	        paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));

	        // Fetch repositories from the database based on filtering criteria and sorting
	        Page<Repositor> pagedResult = repoRepository.findAll(spec, paging);

	        return pagedResult;
	    }
	}

	private Page<Repositor> paginate(List<Repositor> repositories, int pageNo, int pageSize) {
	    int start = pageNo * pageSize;
	    int end = Math.min(start + pageSize, repositories.size());
	    List<Repositor> pageContent = repositories.subList(start, end);
	    return new PageImpl<>(pageContent, PageRequest.of(pageNo, pageSize), repositories.size());
	}

	private List<Repositor> applyFiltering(List<Repositor> repositories, FilterRepos filterRepos) {
	    List<Repositor> filteredRepositories = new ArrayList<>(repositories);
	    // Apply filtering logic here based on filterRepos
	    // For example:
	    System.out.println(filterRepos.getHasLanguage());
	    if (filterRepos.getHasLanguage().length() > 0) {
	    	System.out.println("in lang" + filteredRepositories.size());
	        filteredRepositories.removeIf(repo -> repo.getLanguage().equals(filterRepos.getHasLanguage()));
	        System.out.println("in lang" + filteredRepositories.size());
	    } 
	    if (!filterRepos.getHasTopic().isEmpty()) {
	    	System.out.println("in topic" + filteredRepositories.size());
	        filteredRepositories.removeIf(repo -> !repo.getTopics().contains(filterRepos.getHasTopic()));
	        System.out.println("in topic" + filteredRepositories.size());
	    }
	    return filteredRepositories;
	}
	
	private List<List<String>> extractLanguagesAndTopics(List<Repositor> repositories) {
        List<List<String>> documents = new ArrayList<>();
        for (Repositor repository : repositories) {
            List<String> repoData = new ArrayList<>();
            // Add languages and topics from the repository to the document
            repoData.add(repository.getLanguage());
            repoData.add(repository.getTopics());
            documents.add(repoData);
        }
        return documents;
    }


}

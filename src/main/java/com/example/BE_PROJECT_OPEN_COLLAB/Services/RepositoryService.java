package com.example.BE_PROJECT_OPEN_COLLAB.Services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.example.BE_PROJECT_OPEN_COLLAB.CosineSimilarityAlgorithm.CosineSimilarity;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.FavouriteLanguage;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.FavouriteTopic;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.Repositor;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.User;
import com.example.BE_PROJECT_OPEN_COLLAB.Repositories.FavouriteLanguagesRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Repositories.FavouriteTopicsRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Repositories.RepoRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Repositories.UserRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.FilterRepos;

@Service
public class RepositoryService {

	@Autowired
	private RepoRepository repoRepository;
	
	@Autowired
	private FavouriteLanguagesRepository favouriteLanguagesRepository;
	
	@Autowired
	private FavouriteTopicsRepository favouriteTopicsRepository;
	
	
	
	@Autowired
	private UserServices userServices;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CosineSimilarity demo;

	//save 1 repo
	public Repositor save(Repositor repo) {
		System.out.println(repoRepository.save(repo));
		try {
			return repoRepository.save(repo);
		} catch (Exception e) {
			return null;
		}
	}
	
	//retrive all repos
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
	
	//get repo by id
	public Repositor getRepoById(Long id) throws Exception{
		
			Repositor repository =repoRepository.findById(id);
			
			if(repository==null)throw new CustomException("repository with "+ id + " doesn't exist");
			
			return repository;	
	}

	
	//helper methods
	static Specification<Repositor> withHasLanguage(String hasLanguage) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("language"), hasLanguage);
	}

	static Specification<Repositor> withTopicContaining(String topic) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("topics"), "%'" + topic + "'%");
	}
	
	
	
	//NOTE- ALL SERVICES BELOW THIS ARE FOR EXACT MATCHING which is not used by default
	

	
	//only for exact matching which is not used by default 
	public Page<Repositor> getReposByProfile(Integer pageNo, Integer pageSize, String sortBy, FilterRepos filterRepos, String[] userLanguages, String[] userTopics, String username) {
	    Pageable paging;
	    
	     if(username.equals("") || username==null) {
	    	 throw new CustomException(username+" not found");
	     }
	     
	     userLanguages =userServices.getFavoriteLanguagesByUsernameFromDb(username);
	     userTopics =userServices.getFavoriteTopicsByUsernameFromDb(username);
	    

	    // Apply filtering if provided
	    Specification<Repositor> spec = Specification.where(null);
	    if (!filterRepos.getHasLanguage().isEmpty()) {
	        spec = spec.and(withHasLanguage(filterRepos.getHasLanguage()));
	    } 
	    if (!filterRepos.getHasTopic().isEmpty()) {
	        spec = spec.and(withTopicContaining(filterRepos.getHasTopic()));
	    }
	    
	    List<Repositor> filteredRepositories = repoRepository.findAll(spec);
	    
	 // Check if custom sorting is applied
	    if ("recommended".equals(sortBy)) {
	    	
	        System.out.println("in recommendation");
	        // Extract languages and topics from the fetched repositories
	        List<List<String>> documents = new ArrayList<>();
	        
	        for(Repositor repository : filteredRepositories) {
	        	documents.add(getRepositoryTechnologies(repository));
	        }

	        // Sort repositories using TF-IDF algorithm based on user languages and topics
	        List<Integer> sortedIndices = demo.sortRepositoriesByCosineSimilarity(userLanguages, userTopics, documents);

	        // Map the sorted indices to the paged result
	        List<Repositor> sortedRepositories = new ArrayList<>();
	        for (int index : sortedIndices) {
	            sortedRepositories.add(filteredRepositories.get(index));
	        }


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

	
	public static String[] convertToArray(String input) {
        // Remove square brackets, single quotes, and leading/trailing spaces
        String cleanInput = input.replaceAll("\\[|\\]|'|\\s", "");

        // Split the cleaned input by comma and optional whitespace
        String[] resultArray = cleanInput.split(",\\s*");

        return resultArray;
    }
	
	//exact matching 
	public List<Repositor> recommendRepositories(String username) {

	    Optional<User> useroptional = userRepository.findById(username);
	    if (useroptional.isEmpty()) {
	        throw new CustomException(username + " not found!");
	    }
	    User user = useroptional.get();

	    // Fetch all repositories from the database
	    List<Repositor> allRepositories = repoRepository.findAll();

	    // Use a HashMap to store scores, as Repositor doesn't have a score field
	    Map<Repositor, Integer> scores = new HashMap<>();

	    // Filter and rank repositories
	    List<Repositor> recommendedRepositories = new ArrayList<>();
	    List<String> UserTechnologies=getUserTechnologies(user);
	    for (Repositor repository : allRepositories) {
	        int matchingTechnologies = 0;
	        
	       // System.out.println(UserTechnologies);
	        for (String userTechnologiesarrayofOneUserRepo : UserTechnologies) {
	        	List<String> RepoTechnologies =getRepositoryTechnologies(repository);
	            if (RepoTechnologies.contains(userTechnologiesarrayofOneUserRepo)) {	
	            	matchingTechnologies++;
	            }
	        	
	        }
	        if (matchingTechnologies > 0) { // Add only if there's a match
	            scores.put(repository, matchingTechnologies); // Store scores in HashMap
	            recommendedRepositories.add(repository);
	        }
	    }
	    
	   
//	     Sort repositories based on their scores in the HashMap
	    recommendedRepositories.sort((r1, r2) -> scores.get(r2) - scores.get(r1)); // Use scores from HashMap for sorting
	    
	    return recommendedRepositories;

	}
	ArrayList<String> getUserTechnologies(User user){
		
		ArrayList<String> list=new ArrayList<>();
		for(FavouriteLanguage ele : favouriteLanguagesRepository.findAllByUser(user)) {
			list.add(ele.getLanguageName());
		}
		
		//issue here
		for(FavouriteTopic ele : user.getFavouriteTopic()) {
				list.add(ele.getTopicname());
		}
		
		return list;
		
	}

	ArrayList<String> getRepositoryTechnologies(Repositor repository){

		ArrayList<String> list=new ArrayList<>();
		
		list.add("'" + repository.getLanguage() + "'");
		List<String>al=stringToList(repository.getTopics());
		for(String it : al) {
			list.add(it);
		}
		return (ArrayList<String>)list;
		
	}
	 //"[ 'abc' , 'cv' , 'er' ]" -->[abc,cv,er]
	public List<String> stringToList(String string) {
        // Remove leading and trailing square brackets and quotes
        String cleanString = string.replaceAll("^\"|\"$", "").replaceAll("^\\[|\\]$", "");
//        
        // Split the string by commas, handling potential spaces around commas
        
        String[] splitArray = cleanString.split(",");
        
        List<String> stringList = new ArrayList<>();
        for (String element : splitArray) {
            stringList.add(element); // Remove quotes after splitting
        }
//        System.out.println(stringList);
        // Return the list of strings
        return stringList;
    }
	
	
}
